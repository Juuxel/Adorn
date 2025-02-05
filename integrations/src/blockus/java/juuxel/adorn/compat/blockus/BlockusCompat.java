package juuxel.adorn.compat.blockus;

import com.google.auto.service.AutoService;
import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.CompatBlockVariantSet;

import java.util.List;

@AutoService(CompatBlockVariantSet.class)
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
