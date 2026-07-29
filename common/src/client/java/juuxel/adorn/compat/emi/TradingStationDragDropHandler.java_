package juuxel.adorn.compat.emi;

import dev.emi.emi.api.EmiDragDropHandler;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import juuxel.adorn.client.gui.screen.TradingStationScreen;
import juuxel.adorn.menu.TradingStationMenu;
import net.minecraft.menu.Slot;

import java.util.List;
import java.util.Optional;

public final class TradingStationDragDropHandler implements EmiDragDropHandler<TradingStationScreen> {
    @Override
    public boolean dropStack(TradingStationScreen screen, EmiIngredient stack, int x, int y) {
        var itemStack = single(stack.getEmiStacks()).map(EmiStack::getItemStack).orElse(null);
        if (itemStack == null || itemStack.isEmpty() || !TradingStationMenu.isValidItem(itemStack)) return false;

        for (var slot : new Slot[] { screen.getMenu().getSellingSlot(), screen.getMenu().getPriceSlot() }) {
            var slotX = slot.x + screen.getPanelX();
            var slotY = slot.y + screen.getPanelY();

            if (slotX <= x && slotX < slotX + 16 && slotY <= y && y < slotY + 16) {
                screen.updateTradeStack(slot, itemStack);
                return true;
            }
        }

        return false;
    }

    private static <T> Optional<T> single(List<T> ts) {
        return ts.size() == 1 ? Optional.of(ts.getFirst()) : Optional.empty();
    }
}
