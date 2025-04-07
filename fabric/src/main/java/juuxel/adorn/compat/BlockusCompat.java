package juuxel.adorn.compat;

import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.CompatBlockVariantSet;

import java.util.List;

public final class BlockusCompat implements CompatBlockVariantSet {
    @Override
    public String getModId() {
        return "blockus";
    }

    @Override
    public List<BlockVariant> getWoodVariants() {
        return List.of(new BlockVariant.Wood("blockus/white_oak"));
    }
}
