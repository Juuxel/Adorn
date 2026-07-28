package juuxel.adorn.platform;

import juuxel.adorn.util.InlineServices;
import juuxel.adorn.util.Services;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import org.jspecify.annotations.Nullable;

@InlineServices
public interface ItemBridge {
    @Nullable ItemStackTemplate getRecipeRemainder(ItemStack stack);

    @InlineServices.Getter
    static ItemBridge get() {
        return Services.load(ItemBridge.class);
    }
}
