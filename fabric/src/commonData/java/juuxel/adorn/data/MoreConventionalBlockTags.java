package juuxel.adorn.data;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

/// Conventional block tags defined by Adorn.
public final class MoreConventionalBlockTags {
    private static final String NAMESPACE = "c";

    public static final TagKey<Block> LANTERNS = of("lanterns");

    private static TagKey<Block> of(String name) {
        return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(NAMESPACE, name));
    }
}
