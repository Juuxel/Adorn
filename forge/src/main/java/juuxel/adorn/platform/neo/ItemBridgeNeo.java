package juuxel.adorn.platform.neo;

import juuxel.adorn.platform.ItemBridge;
import net.minecraft.item.ItemStack;

public final class ItemBridgeNeo implements ItemBridge {
    @Override
    public ItemStack getRecipeRemainder(ItemStack stack) {
        return stack.getCraftingRemainder();
    }
}
