package juuxel.adorn.compat.promenade;

import com.google.auto.service.AutoService;
import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.BlockVariantSet;
import juuxel.adorn.block.variant.CompatBlockVariantSet;

import java.util.List;

@AutoService(BlockVariantSet.class)
public final class PromenadeCompat extends CompatBlockVariantSet {
    @Override
    protected String getModId() {
        return "promenade";
    }

    @Override
    public List<BlockVariant> getWoodVariants() {
        return createVariants(
            BlockVariant.Wood::new,
            "dark_amaranth",
            "palm",
            "sakura"
        );
    }
}
