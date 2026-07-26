package juuxel.adorn.data;

import net.minecraft.world.item.Item;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.Identifier;

/** Conventional item tags defined by Adorn. */
public final class MoreConventionalItemTags {
    private static final String NAMESPACE = "c";

    public static final TagKey<Item> GLOW_BERRY_FOODS = of("foods/berry/glow");
    public static final TagKey<Item> SWEET_BERRY_FOODS = of("foods/berry/sweet");
    public static final TagKey<Item> MILK_FOODS = of("foods/milk");
    public static final TagKey<Item> HONEYCOMBS = of("honeycombs");
    public static final TagKey<Item> COPPER_NUGGETS = of("nuggets/copper");
    public static final TagKey<Item> STONE_RODS = of("rods/stone");
    public static final TagKey<Item> BOOKS = of("books");

    private static TagKey<Item> of(String name) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(NAMESPACE, name));
    }
}
