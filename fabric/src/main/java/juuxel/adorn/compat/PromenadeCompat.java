package juuxel.adorn.compat;

import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.PrefixedBlockVariantSet;

import java.util.List;

public final class PromenadeCompat extends PrefixedBlockVariantSet {
    @Override
    public String getModId() {
        return "promenade";
    }

    @Override
    public List<BlockVariant> getWoodVariants() {
        return createVariants(
            BlockVariant.Wood::new,
            "dark_amaranth",
            "palm",
            "sakura"
        );
    }
}
