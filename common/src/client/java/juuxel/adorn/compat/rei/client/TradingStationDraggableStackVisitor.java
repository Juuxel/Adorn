package juuxel.adorn.compat.rei.client;

import com.mojang.datafixers.util.Pair;
import juuxel.adorn.client.gui.screen.TradingStationScreen;
import juuxel.adorn.menu.TradingStationMenu;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.drag.DraggableStack;
import me.shedaniel.rei.api.client.gui.drag.DraggableStackVisitor;
import me.shedaniel.rei.api.client.gui.drag.DraggedAcceptorResult;
import me.shedaniel.rei.api.client.gui.drag.DraggingContext;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.stream.Stream;

public final class TradingStationDraggableStackVisitor implements DraggableStackVisitor<TradingStationScreen> {
    private Stream<Pair<Slot, Rectangle>> slots(DraggingContext<TradingStationScreen> context) {
        var menu = context.getScreen().getMenu();
        return Stream.of(menu.getSellingSlot(), menu.getPriceSlot())
            .map(slot -> Pair.of(slot, new Rectangle(slot.x, slot.y, 16, 16)))
            .peek(pair -> pair.getSecond().translate(context.getScreen().getPanelX(), context.getScreen().getPanelY()));
    }

    @Override
    public <R extends Screen> boolean isHandingScreen(R screen) {
        return screen instanceof TradingStationScreen;
    }

    private static boolean isValidStack(DraggableStack stack) {
        var entryStack = stack.getStack();
        if (!entryStack.getType().equals(VanillaEntryTypes.ITEM)) return false;
        ItemStack itemStack = entryStack.castValue();
        return TradingStationMenu.isValidItem(itemStack);
    }

    @Override
    public DraggedAcceptorResult acceptDraggedStack(DraggingContext<TradingStationScreen> context, DraggableStack stack) {
        // Check that we're handling a valid item, no need to run any code for other entry types
        if (!isValidStack(stack)) return DraggedAcceptorResult.PASS;

        var pos = context.getCurrentPosition();
        if (pos == null) return DraggedAcceptorResult.PASS;
        var slot = slots(context)
            .filter(pair -> pair.getSecond().contains(pos))
            .findAny()
            .map(Pair::getFirst)
            .orElse(null);

        if (slot != null) {
            context.getScreen().updateTradeStack(slot, stack.getStack().castValue());
            return DraggedAcceptorResult.CONSUMED;
        }

        return DraggedAcceptorResult.PASS;
    }

    @Override
    public Stream<BoundsProvider> getDraggableAcceptingBounds(DraggingContext<TradingStationScreen> context, DraggableStack stack) {
        if (!isValidStack(stack)) return Stream.empty();
        return slots(context)
            .map(Pair::getSecond)
            .map(BoundsProvider::ofRectangle);
    }
}
