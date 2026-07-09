package juuxel.adorn.compat;

import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.CompatBlockVariantSet;

import java.util.List;

public final class BiomesOPlentyCompat extends CompatBlockVariantSet {
    @Override
    protected String getModId() {
        return "biomesoplenty";
    }

    @Override
    public List<BlockVariant> getWoodVariants() {
        return createVariants(
            BlockVariant.Wood::new,
            "dead",
            "empyreal",
            "fir",
            "hellbark",
            "jacaranda",
            "magic",
            "mahogany",
            "maple",
            "palm",
            "pine",
            "redwood",
            "umbran",
            "willow"
        );
    }

    @Override
    public List<BlockVariant> getStoneVariants() {
        return createVariants(
            BlockVariant.Stone::new,
            "black_sandstone",
            "cut_black_sandstone",
            "smooth_black_sandstone",
            "orange_sandstone",
            "cut_orange_sandstone",
            "smooth_orange_sandstone",
            "white_sandstone",
            "cut_white_sandstone",
            "smooth_white_sandstone"
        );
    }
}
