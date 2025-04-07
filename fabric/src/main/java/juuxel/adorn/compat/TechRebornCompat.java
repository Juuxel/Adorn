package juuxel.adorn.compat;

import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.CompatBlockVariantSet;

import java.util.List;

public final class TechRebornCompat implements CompatBlockVariantSet {
    @Override
    public String getModId() {
        return "techreborn";
    }

    @Override
    public List<BlockVariant> getWoodVariants() {
        return List.of(new BlockVariant.Wood("techreborn/rubber"));
    }
}
