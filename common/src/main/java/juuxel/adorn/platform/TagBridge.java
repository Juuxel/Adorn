package juuxel.adorn.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public final class TagBridge {
    @ExpectPlatform
    @NotNull
    public static Tag<Block> block(Identifier id) {
        return PlatformCore.expected();
    }

    @ExpectPlatform
    @NotNull
    public static Tag<Item> item(Identifier id) {
        return PlatformCore.expected();
    }
}
