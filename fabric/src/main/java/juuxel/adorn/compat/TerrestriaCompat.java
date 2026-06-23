package juuxel.adorn.compat;

import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.PrefixedBlockVariantSet;

import java.util.List;

public final class TerrestriaCompat extends PrefixedBlockVariantSet {
    @Override
    public String getModId() {
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
            "volcanic_rock",
            "volcanic_rock_cobblestone",
            "smooth_volcanic_rock",
            "volcanic_rock_brick",
            "mossy_volcanic_rock_cobblestone",
            "mossy_volcanic_rock_brick"
        );
    }
}
