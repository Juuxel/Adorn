package juuxel.adorn.compat.jei;

import juuxel.adorn.client.gui.screen.TradingStationScreen;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.ingredients.ITypedIngredient;
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.item.ItemStack;
import net.minecraft.menu.Slot;

import java.util.ArrayList;
import java.util.List;

final class TradingStationGhostIngredientHandler implements IGhostIngredientHandler<TradingStationScreen> {
    @Override
    public <I> List<Target<I>> getTargetsTyped(TradingStationScreen gui, ITypedIngredient<I> ingredient, boolean doStart) {
        if (ingredient.getType() == VanillaTypes.ITEM_STACK) {
            ItemStack stack = ingredient.getItemStack().orElse(ItemStack.EMPTY);
            if (!stack.isEmpty()) {
                List<Target<I>> targets = new ArrayList<>();
                for (Slot slot : new Slot[] { gui.getMenu().getSellingSlot(), gui.getMenu().getPriceSlot() }) {
                    int x = slot.x + gui.getPanelX();
                    int y = slot.y + gui.getPanelY();
                    targets.add(new Target<>() {
                        @Override
                        public Rect2i getArea() {
                            return new Rect2i(x, y, 16, 16);
                        }

                        @Override
                        public void accept(I ingredient) {
                            gui.updateTradeStack(slot, (ItemStack) ingredient);
                        }
                    });
                }
                return targets;
            }
        }

        return List.of();
    }

    @Override
    public void onComplete() {
    }
}
