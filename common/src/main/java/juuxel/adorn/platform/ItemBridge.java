package juuxel.adorn.platform;

import juuxel.adorn.entity.ConeVariant;
import juuxel.adorn.item.ConeItem;
import juuxel.adorn.util.InlineServices;
import juuxel.adorn.util.Services;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@InlineServices
public interface ItemBridge {
    ItemStack getRecipeRemainder(ItemStack stack);

    ConeItem createConeItem(ConeVariant variant, Item.Settings settings);

    @InlineServices.Getter
    static ItemBridge get() {
        return Services.load(ItemBridge.class);
    }
}
