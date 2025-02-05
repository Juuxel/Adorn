package juuxel.adorn.compat.terrestria;

import com.google.auto.service.AutoService;
import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.BlockVariantSet;
import juuxel.adorn.block.variant.CompatBlockVariantSet;

import java.util.List;

@AutoService(BlockVariantSet.class)
public final class TerrestriaCompat extends CompatBlockVariantSet {
    @Override
    protected String getModId() {
        return "terrestria";
    }

    @Override
    public List<BlockVariant> getWoodVariants() {
        return createVariants(
            BlockVariant.Wood::new,
            "cypress",
            "hemlock",
            "japanese_maple",
            "rainbow_eucalyptus",
            "redwood",
            "rubber",
            "sakura",
            "yucca_palm",
            "willow"
        );
    }

    @Override
    public List<BlockVariant> getStoneVariants() {
        return createVariants(
            BlockVariant.Stone::new,
            "basalt",
            "basalt_cobblestone",
            "smooth_basalt",
            "basalt_brick",
            "mossy_basalt_cobblestone",
            "mossy_basalt_brick"
        );
    }
}
