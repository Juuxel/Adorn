package juuxel.adorn.compat.biomemakeover;

import com.google.auto.service.AutoService;
import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.BlockVariantSet;
import juuxel.adorn.block.variant.CompatBlockVariantSet;

import java.util.List;

@AutoService(BlockVariantSet.class)
public final class BiomeMakeoverCompat extends CompatBlockVariantSet {
    @Override
    protected String getModId() {
        return "biomemakeover";
    }

    @Override
    public List<BlockVariant> getWoodVariants() {
        return createVariants(
            BlockVariant.Wood::new,
            "ancient_oak",
            "blighted_balsa",
            "willow",
            "swamp_cypress"
        );
    }
}
