package juuxel.adorn.platform.neo;

import juuxel.adorn.entity.ConeVariant;
import juuxel.adorn.item.ConeItem;
import juuxel.adorn.platform.ItemBridge;
import juuxel.adorn.platform.neo.item.ConeItemNeo;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class ItemBridgeNeo implements ItemBridge {
    @Override
    public ItemStack getRecipeRemainder(ItemStack stack) {
        return stack.hasCraftingRemainingItem() ? stack.getCraftingRemainingItem() : ItemStack.EMPTY;
    }

    @Override
    public ConeItem createConeItem(ConeVariant variant, Item.Settings settings) {
        return new ConeItemNeo(variant, settings);
    }
}
