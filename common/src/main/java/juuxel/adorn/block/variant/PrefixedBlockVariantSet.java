package juuxel.adorn.block.variant;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public abstract class PrefixedBlockVariantSet implements CompatBlockVariantSet {
    protected List<BlockVariant> createVariants(Function<String, BlockVariant> factory, String... variants) {
        return Arrays.stream(variants).map(variant -> factory.apply(getModId() + '/' + variant)).toList();
    }
}
