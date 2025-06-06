package juuxel.adorn.platform.fabric;

import juuxel.adorn.entity.ConeVariant;
import juuxel.adorn.item.ConeItem;
import juuxel.adorn.platform.ItemBridge;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class ItemBridgeFabric implements ItemBridge {
    @Override
    public ItemStack getRecipeRemainder(ItemStack stack) {
        return stack.getRecipeRemainder();
    }

    @Override
    public ConeItem createConeItem(ConeVariant variant, Item.Settings settings) {
        return new ConeItem(variant, settings.equipmentSlot((entity, stack) -> EquipmentSlot.HEAD));
    }
}
