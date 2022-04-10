package juuxel.adorn.item;

import juuxel.adorn.lib.AdornTags;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;

import java.util.Set;

public record FuelData(TagKey<Item> tag, int burnTime) {
    // TODO: Crates should burn
    public static final Set<FuelData> FUEL_DATA = Set.of(
        // Wooden (300)
        new FuelData(AdornTags.CHAIRS.getItem(), 300),
        new FuelData(AdornTags.DRAWERS.getItem(), 300),
        new FuelData(AdornTags.TABLES.getItem(), 300),
        new FuelData(AdornTags.BENCHES.getItem(), 300),
        new FuelData(AdornTags.WOODEN_POSTS.getItem(), 300),
        new FuelData(AdornTags.WOODEN_PLATFORMS.getItem(), 300),
        new FuelData(AdornTags.WOODEN_STEPS.getItem(), 300),
        new FuelData(AdornTags.WOODEN_SHELVES.getItem(), 300),
        // Woollen (150)
        new FuelData(AdornTags.SOFAS.getItem(), 150)
    );
}
