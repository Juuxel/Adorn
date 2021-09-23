package juuxel.adorn.item;

import juuxel.adorn.block.BenchBlock;
import juuxel.adorn.block.ChairBlock;
import juuxel.adorn.block.DrawerBlock;
import juuxel.adorn.block.TableBlock;
import juuxel.adorn.lib.AdornTags;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.tag.Tag;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public record FuelData(@Nullable Class<? extends ItemConvertible> itemOrBlockType, Tag<Item> tag, int burnTime) {
    public static final Set<FuelData> FUEL_DATA = Set.of(
        // Wooden (300)
        new FuelData(ChairBlock.class, AdornTags.CHAIRS.getItem(), 300),
        new FuelData(DrawerBlock.class, AdornTags.DRAWERS.getItem(), 300),
        new FuelData(TableBlock.class, AdornTags.TABLES.getItem(), 300),
        new FuelData(BenchBlock.class, AdornTags.BENCHES.getItem(), 300),
        // Wooden variants of any-material stuff (300), not available on Forge
        new FuelData(null, AdornTags.WOODEN_POSTS.getItem(), 300),
        new FuelData(null, AdornTags.WOODEN_PLATFORMS.getItem(), 300),
        new FuelData(null, AdornTags.WOODEN_STEPS.getItem(), 300),
        new FuelData(null, AdornTags.WOODEN_SHELVES.getItem(), 300),
        // Woollen (150)
        new FuelData(null, AdornTags.SOFAS.getItem(), 150)
    );
}
