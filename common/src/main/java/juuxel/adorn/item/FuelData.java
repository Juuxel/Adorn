package juuxel.adorn.item;

import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.lib.AdornTags;
import juuxel.adorn.lib.registry.Registered;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.TagKey;

import java.util.Set;

public sealed interface FuelData {
    Set<FuelData> FUEL_DATA = Set.of(
        // Wooden (300)
        new ForTag(AdornTags.CHAIRS.item(), 300),
        new ForTag(AdornTags.DRAWERS.item(), 300),
        new ForTag(AdornTags.TABLES.item(), 300),
        new ForTag(AdornTags.BENCHES.item(), 300),
        new ForTag(AdornTags.WOODEN_POSTS.item(), 300),
        new ForTag(AdornTags.WOODEN_PLATFORMS.item(), 300),
        new ForTag(AdornTags.WOODEN_STEPS.item(), 300),
        new ForTag(AdornTags.WOODEN_SHELVES.item(), 300),
        new ForItem(AdornBlocks.CRATE, 300),
        // Woollen (150)
        new ForTag(AdornTags.SOFAS.item(), 150)
    );

    int burnTime();
    boolean matches(ItemStack stack);

    record ForItem(Registered<? extends ItemLike> item, int burnTime) implements FuelData {
        @Override
        public boolean matches(ItemStack stack) {
            return stack.is(item.get().asItem());
        }
    }

    record ForTag(
        TagKey<Item> tag, int burnTime) implements FuelData {
        @Override
        public boolean matches(ItemStack stack) {
            return stack.is(tag);
        }
    }
}
