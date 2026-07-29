package juuxel.adorn.data;

import juuxel.adorn.item.AdornItems;
import juuxel.adorn.lib.AdornTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.references.BlockItemIds;
import net.minecraft.references.ItemIds;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;

import java.util.concurrent.CompletableFuture;

public final class AdornItemTagGenerator extends FabricTagsProvider.ItemTagsProvider {
    public AdornItemTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture, FabricTagsProvider.BlockTagsProvider blockTagProvider) {
        super(output, registriesFuture, blockTagProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        copy(AdornTags.PAINTED_PLANKS);
        copy(AdornTags.PAINTED_WOOD_SLABS);
        copy(AdornTags.PAINTED_WOOD_STAIRS);
        copy(AdornTags.PAINTED_WOOD_FENCES);
        copy(AdornTags.PAINTED_WOOD_FENCE_GATES);
        copy(AdornTags.PAINTED_WOOD_PRESSURE_PLATES);
        copy(AdornTags.PAINTED_WOOD_BUTTONS);
        copy(AdornTags.PAINTED_CHAIRS);
        copy(AdornTags.PAINTED_TABLES);
        copy(AdornTags.PAINTED_DRAWERS);
        copy(AdornTags.PAINTED_BENCHES);
        copy(AdornTags.PAINTED_KITCHEN_COUNTERS);
        copy(AdornTags.PAINTED_KITCHEN_CUPBOARDS);
        copy(AdornTags.PAINTED_KITCHEN_SINKS);
        copy(AdornTags.PAINTED_WOOD_POSTS);
        copy(AdornTags.PAINTED_WOOD_PLATFORMS);
        copy(AdornTags.PAINTED_WOOD_STEPS);
        copy(AdornTags.PAINTED_WOOD_SHELVES);
        copy(AdornTags.PAINTED_COFFEE_TABLES);
        copy(AdornTags.PLATFORMS);
        copy(AdornTags.POSTS);
        copy(AdornTags.STEPS);
        copy(AdornTags.SHELVES);
        copy(AdornTags.CANDLELIT_LANTERNS);
        copy(AdornTags.COPPER_PIPES);
        copy(AdornTags.KITCHEN_BLOCKS);
        copy(AdornTags.CHIMNEYS);
        copy(AdornTags.PRISMARINE_CHIMNEYS);
        copy(AdornTags.REGULAR_CHIMNEYS);
        copy(AdornTags.CRATES);
        copy(AdornTags.FILLED_CRATES);

        builder(AdornTags.CAUTION_SIGNS.item())
            .add(AdornItems.CAUTION_SIGN.key())
            .add(AdornItems.BEE_CAUTION_SIGN.key())
            .add(AdornItems.BOOK_CAUTION_SIGN.key())
            .add(AdornItems.CLIFF_CAUTION_SIGN.key())
            .add(AdornItems.FORBIDDEN_CAUTION_SIGN.key())
            .add(AdornItems.HELMET_CAUTION_SIGN.key())
            .add(AdornItems.RAILS_CAUTION_SIGN.key())
            .add(AdornItems.SURPRISE_CAUTION_SIGN.key());

        builder(AdornTags.BREWING_INPUTS)
            .add(AdornItems.MUG.key());

        builder(AdornTags.FURNITURE_DYES)
            .forceAddTag(ItemTags.DYES);

        builder(AdornTags.WATERING_CAN_FERTILIZERS)
            .forceAddTag(ConventionalItemTags.FERTILIZERS);

        addVanillaTags();
        addConventionalTags();
    }

    private void addVanillaTags() {
        copy(BlockTags.PLANKS, ItemTags.PLANKS);
        copy(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS);
        copy(BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS);
        copy(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
        copy(BlockTags.FENCE_GATES, ItemTags.FENCE_GATES);
        copy(BlockTags.WOODEN_PRESSURE_PLATES, ItemTags.WOODEN_PRESSURE_PLATES);
        copy(BlockTags.WOODEN_BUTTONS, ItemTags.WOODEN_BUTTONS);

        builder(ItemTags.BOOKSHELF_BOOKS)
            .add(AdornItems.GUIDE_BOOK.key(), AdornItems.TRADERS_MANUAL.key());
    }

    private void addConventionalTags() {
        copy(MoreConventionalBlockTags.LANTERNS, MoreConventionalItemTags.LANTERNS);

        builder(MoreConventionalItemTags.BOOKS)
            // Vanilla items
            .add(ItemIds.BOOK, ItemIds.ENCHANTED_BOOK, ItemIds.WRITABLE_BOOK, ItemIds.WRITTEN_BOOK)
            // Adorn items
            .add(AdornItems.GUIDE_BOOK.key(), AdornItems.TRADERS_MANUAL.key());

        builder(ConventionalItemTags.BERRY_FOODS)
            .addTag(MoreConventionalItemTags.GLOW_BERRY_FOODS)
            .addTag(MoreConventionalItemTags.SWEET_BERRY_FOODS);

        builder(MoreConventionalItemTags.GLOW_BERRY_FOODS)
            .add(BlockItemIds.GLOW_BERRY_CROP);

        builder(MoreConventionalItemTags.SWEET_BERRY_FOODS)
            .add(BlockItemIds.SWEET_BERRY_CROP);

        builder(MoreConventionalItemTags.COFFEE_DRINKS)
            .add(AdornItems.NETHER_WART_COFFEE.key());

        builder(MoreConventionalItemTags.TEA_DRINKS)
            .add(AdornItems.GLOW_BERRY_TEA.key());

        builder(ConventionalItemTags.JUICE_DRINKS)
            .add(AdornItems.SWEET_BERRY_JUICE.key());

        builder(ConventionalItemTags.DRINKS)
            .addTag(MoreConventionalItemTags.COFFEE_DRINKS)
            .addTag(MoreConventionalItemTags.TEA_DRINKS);

        builder(MoreConventionalItemTags.STONE_RODS)
            .add(AdornItems.STONE_ROD.key());

        builder(ConventionalItemTags.RODS)
            .addTag(MoreConventionalItemTags.STONE_RODS);

        builder(MoreConventionalItemTags.HONEYCOMBS)
            .add(ItemIds.HONEYCOMB);
    }

    private void copy(AdornTags.TagPair tagPair) {
        copy(tagPair.block(), tagPair.item());
    }
}
