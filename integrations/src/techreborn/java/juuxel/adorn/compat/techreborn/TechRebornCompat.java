package juuxel.adorn.compat.techreborn;

import com.google.auto.service.AutoService;
import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.CompatBlockVariantSet;

import java.util.List;

@AutoService(CompatBlockVariantSet.class)
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
