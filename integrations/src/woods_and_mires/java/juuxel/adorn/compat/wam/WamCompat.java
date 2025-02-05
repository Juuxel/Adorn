package juuxel.adorn.compat.wam;

import com.google.auto.service.AutoService;
import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.BlockVariantSet;

import java.util.List;

@AutoService(BlockVariantSet.class)
public final class WamCompat implements BlockVariantSet {
    @Override
    public List<BlockVariant> getWoodVariants() {
        return List.of(new BlockVariant.Wood("woods_and_mires/pine"));
    }
}
