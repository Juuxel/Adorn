package juuxel.adorn.compat;

import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.CompatBlockVariantSet;

import java.util.List;

public final class TraverseCompat implements CompatBlockVariantSet {
    @Override
    public String getModId() {
        return "traverse";
    }

    @Override
    public List<BlockVariant> getWoodVariants() {
        return List.of(new BlockVariant.Wood("traverse/fir"));
    }
}
