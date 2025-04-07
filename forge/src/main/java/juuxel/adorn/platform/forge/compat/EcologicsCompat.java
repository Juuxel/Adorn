package juuxel.adorn.platform.forge.compat;

import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.PrefixedBlockVariantSet;

import java.util.List;

public final class EcologicsCompat extends PrefixedBlockVariantSet {
    @Override
    public String getModId() {
        return "ecologics";
    }

    @Override
    public List<BlockVariant> getWoodVariants() {
        return createVariants(
            BlockVariant.Wood::new,
            "azalea",
            "flowering_azalea",
            "coconut",
            "walnut"
        );
    }
}
