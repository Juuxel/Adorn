package juuxel.adorn.platform.fabric;

import juuxel.adorn.platform.ItemBridge;
import net.minecraft.world.item.ItemStack;

public final class ItemBridgeFabric implements ItemBridge {
    @Override
    public ItemStack getRecipeRemainder(ItemStack stack) {
        return stack.getRecipeRemainder();
    }
}
