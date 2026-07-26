package juuxel.adorn.platform.fabric;

import juuxel.adorn.platform.ItemBridge;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;

public final class ItemBridgeFabric implements ItemBridge {
    @Override
    public ItemStackTemplate getRecipeRemainder(ItemStack stack) {
        return stack.getCraftingRemainder();
    }
}
