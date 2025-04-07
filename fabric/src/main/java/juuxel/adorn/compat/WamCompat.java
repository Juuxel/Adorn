package juuxel.adorn.compat;

import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.CompatBlockVariantSet;

import java.util.List;

public final class WamCompat implements CompatBlockVariantSet {
    @Override
    public String getModId() {
        return "woods_and_mires";
    }

    @Override
    public List<BlockVariant> getWoodVariants() {
        return List.of(new BlockVariant.Wood("woods_and_mires/pine"));
    }
}
