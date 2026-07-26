package juuxel.adorn.platform;

import juuxel.adorn.util.InlineServices;
import juuxel.adorn.util.Services;
import net.minecraft.world.item.ItemStack;

@InlineServices
public interface ItemBridge {
    ItemStack getRecipeRemainder(ItemStack stack);

    @InlineServices.Getter
    static ItemBridge get() {
        return Services.load(ItemBridge.class);
    }
}
