package juuxel.adorn.platform.neo.item;

import juuxel.adorn.entity.ConeVariant;
import juuxel.adorn.item.ConeItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public final class ConeItemNeo extends ConeItem {
    public ConeItemNeo(ConeVariant variant, Settings settings) {
        super(variant, settings);
    }

    @Override
    public EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EquipmentSlot.HEAD;
    }
}
