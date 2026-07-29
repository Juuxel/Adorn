package juuxel.adorn.data;

import juuxel.adorn.item.AdornItems;
import juuxel.adorn.lib.AdornTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;

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

        valueLookupBuilder(AdornTags.CAUTION_SIGNS.item())
            .add(AdornItems.CAUTION_SIGN.get())
            .add(AdornItems.BEE_CAUTION_SIGN.get())
            .add(AdornItems.BOOK_CAUTION_SIGN.get())
            .add(AdornItems.CLIFF_CAUTION_SIGN.get())
            .add(AdornItems.FORBIDDEN_CAUTION_SIGN.get())
            .add(AdornItems.HELMET_CAUTION_SIGN.get())
            .add(AdornItems.RAILS_CAUTION_SIGN.get())
            .add(AdornItems.SURPRISE_CAUTION_SIGN.get());

        valueLookupBuilder(AdornTags.BREWING_INPUTS)
            .add(AdornItems.MUG.get());

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

        valueLookupBuilder(ItemTags.BOOKSHELF_BOOKS)
            .add(AdornItems.GUIDE_BOOK.get(), AdornItems.TRADERS_MANUAL.get());
    }

    private void addConventionalTags() {
        copy(MoreConventionalBlockTags.LANTERNS, MoreConventionalItemTags.LANTERNS);

        valueLookupBuilder(MoreConventionalItemTags.BOOKS)
            // Vanilla items
            .add(Items.BOOK, Items.ENCHANTED_BOOK, Items.WRITABLE_BOOK, Items.WRITTEN_BOOK)
            // Adorn items
            .add(AdornItems.GUIDE_BOOK.get(), AdornItems.TRADERS_MANUAL.get());

        builder(ConventionalItemTags.BERRY_FOODS)
            .addTag(MoreConventionalItemTags.GLOW_BERRY_FOODS)
            .addTag(MoreConventionalItemTags.SWEET_BERRY_FOODS);

        valueLookupBuilder(MoreConventionalItemTags.GLOW_BERRY_FOODS)
            .add(Items.GLOW_BERRIES);

        valueLookupBuilder(MoreConventionalItemTags.SWEET_BERRY_FOODS)
            .add(Items.SWEET_BERRIES);

        valueLookupBuilder(MoreConventionalItemTags.COFFEE_FOODS)
            .add(AdornItems.NETHER_WART_COFFEE.get());

        valueLookupBuilder(MoreConventionalItemTags.TEA_FOODS)
            .add(AdornItems.GLOW_BERRY_TEA.get());

        valueLookupBuilder(MoreConventionalItemTags.JUICE_FOODS)
            .add(AdornItems.SWEET_BERRY_JUICE.get());

        builder(MoreConventionalItemTags.MILK_FOODS)
            .forceAddTag(ConventionalItemTags.MILK_BUCKETS)
            .addOptionalTag(TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "milks")));

        valueLookupBuilder(ConventionalItemTags.FOODS)
            .addTag(MoreConventionalItemTags.COFFEE_FOODS)
            .addTag(MoreConventionalItemTags.JUICE_FOODS)
            .addTag(MoreConventionalItemTags.MILK_FOODS)
            .addTag(MoreConventionalItemTags.TEA_FOODS);

        valueLookupBuilder(ConventionalItemTags.COPPER_NUGGETS)
            .add(AdornItems.COPPER_NUGGET.get());

        valueLookupBuilder(MoreConventionalItemTags.STONE_RODS)
            .add(AdornItems.STONE_ROD.get());

        builder(ConventionalItemTags.RODS)
            .addTag(MoreConventionalItemTags.STONE_RODS);

        valueLookupBuilder(MoreConventionalItemTags.HONEYCOMBS)
            .add(Items.HONEYCOMB);
    }

    private void copy(AdornTags.TagPair tagPair) {
        copy(tagPair.block(), tagPair.item());
    }
}
