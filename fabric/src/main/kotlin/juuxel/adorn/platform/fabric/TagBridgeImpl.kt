package juuxel.adorn.platform.fabric;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public final class TagBridgeImpl {
    @NotNull
    public static Tag<Block> block(Identifier id) {
        return TagRegistry.block(id);
    }

    @NotNull
    public static Tag<Item> item(Identifier id) {
        return TagRegistry.item(id);
    }
}
