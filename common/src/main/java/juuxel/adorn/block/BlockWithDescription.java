package juuxel.adorn.block;

import juuxel.adorn.component.AdornComponentTypes;
import juuxel.adorn.component.ItemDescription;
import net.minecraft.world.level.block.Block;

/**
 * Can be added to a block to provide a description for the block item
 * when registered using {@link juuxel.adorn.lib.registry.RegistryHelper#registerBlock}.
 */
public interface BlockWithDescription extends BlockWithItemComponents {
    default String getDescriptionKey() {
        return ((Block) this).getDescriptionId() + ".description";
    }

    @Override
    default void addItemComponents(ComponentConsumer consumer) {
        consumer.add(AdornComponentTypes.DESCRIPTION, ItemDescription.ofTranslation(getDescriptionKey()));
    }
}
