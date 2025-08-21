package juuxel.adorn.block;

import net.minecraft.block.Block;

/**
 * Can be added to a block to provide a description for the block item
 * when registered using {@link juuxel.adorn.lib.registry.RegistryHelper#registerBlock}.
 */
public interface BlockWithDescription {
    default String getDescriptionKey() {
        return ((Block) this).getTranslationKey() + ".description";
    }
}
