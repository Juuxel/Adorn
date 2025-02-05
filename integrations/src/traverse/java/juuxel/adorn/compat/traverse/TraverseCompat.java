package juuxel.adorn.compat.traverse;

import com.google.auto.service.AutoService;
import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.CompatBlockVariantSet;

import java.util.List;

@AutoService(CompatBlockVariantSet.class)
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
