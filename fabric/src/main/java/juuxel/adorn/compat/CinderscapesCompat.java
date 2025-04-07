package juuxel.adorn.compat;

import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.PrefixedBlockVariantSet;

import java.util.List;

public final class CinderscapesCompat extends PrefixedBlockVariantSet {
    @Override
    public String getModId() {
        return "cinderscapes";
    }

    @Override
    public List<BlockVariant> getWoodVariants() {
        return createVariants(
            BlockVariant.Wood::new,
            "scorched",
            "umbral"
        );
    }
}
