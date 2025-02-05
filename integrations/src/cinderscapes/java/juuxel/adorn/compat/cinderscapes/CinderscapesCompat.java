package juuxel.adorn.compat.cinderscapes;

import com.google.auto.service.AutoService;
import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.BlockVariantSet;
import juuxel.adorn.block.variant.CompatBlockVariantSet;

import java.util.List;

@AutoService(BlockVariantSet.class)
public final class CinderscapesCompat extends CompatBlockVariantSet {
    @Override
    protected String getModId() {
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
