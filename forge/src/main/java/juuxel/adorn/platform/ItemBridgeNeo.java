package juuxel.adorn.platform;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import org.jspecify.annotations.Nullable;

public final class ItemBridgeNeo implements ItemBridge {
    @Override
    public @Nullable ItemStackTemplate getRecipeRemainder(ItemStack stack) {
        return stack.getCraftingRemainder();
    }
}
