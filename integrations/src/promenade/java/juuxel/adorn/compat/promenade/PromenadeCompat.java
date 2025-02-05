package juuxel.adorn.compat.promenade;

import com.google.auto.service.AutoService;
import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.CompatBlockVariantSet;
import juuxel.adorn.block.variant.PrefixedBlockVariantSet;

import java.util.List;

@AutoService(CompatBlockVariantSet.class)
public final class PromenadeCompat extends PrefixedBlockVariantSet {
    @Override
    public String getModId() {
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
