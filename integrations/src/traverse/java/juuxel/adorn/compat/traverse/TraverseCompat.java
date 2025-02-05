package juuxel.adorn.compat.traverse;

import com.google.auto.service.AutoService;
import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.BlockVariantSet;

import java.util.List;

@AutoService(BlockVariantSet.class)
public final class TraverseCompat implements BlockVariantSet {
    @Override
    public List<BlockVariant> getWoodVariants() {
        return List.of(new BlockVariant.Wood("traverse/fir"));
    }
}
