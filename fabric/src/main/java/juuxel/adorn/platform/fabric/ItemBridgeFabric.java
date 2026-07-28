package juuxel.adorn.platform.fabric;

import juuxel.adorn.platform.ItemBridge;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import org.jspecify.annotations.Nullable;

public final class ItemBridgeFabric implements ItemBridge {
    @Override
    public @Nullable ItemStackTemplate getRecipeRemainder(ItemStack stack) {
        return stack.getCraftingRemainder();
    }
}
