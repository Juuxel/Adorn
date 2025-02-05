package juuxel.adorn.compat.cinderscapes;

import com.google.auto.service.AutoService;
import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.CompatBlockVariantSet;
import juuxel.adorn.block.variant.PrefixedBlockVariantSet;

import java.util.List;

@AutoService(CompatBlockVariantSet.class)
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
