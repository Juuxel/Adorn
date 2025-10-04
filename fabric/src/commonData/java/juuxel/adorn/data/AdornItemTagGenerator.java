package juuxel.adorn.data;

import juuxel.adorn.item.AdornItems;
import juuxel.adorn.lib.AdornTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public final class AdornItemTagGenerator extends FabricTagProvider.ItemTagProvider {
    public AdornItemTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture, FabricTagProvider.BlockTagProvider blockTagProvider) {
        super(output, registriesFuture, blockTagProvider);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
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
        copy(BlockTags.PLANKS, ItemTags.PLANKS);
        copy(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS);
        copy(BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS);
        copy(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
        copy(BlockTags.FENCE_GATES, ItemTags.FENCE_GATES);
        copy(BlockTags.WOODEN_PRESSURE_PLATES, ItemTags.WOODEN_PRESSURE_PLATES);
        copy(BlockTags.WOODEN_BUTTONS, ItemTags.WOODEN_BUTTONS);

        valueLookupBuilder(AdornTags.CAUTION_SIGNS.item())
            .add(AdornItems.CAUTION_SIGN.get())
            .add(AdornItems.BEE_CAUTION_SIGN.get())
            .add(AdornItems.BOOK_CAUTION_SIGN.get())
            .add(AdornItems.CLIFF_CAUTION_SIGN.get())
            .add(AdornItems.FORBIDDEN_CAUTION_SIGN.get())
            .add(AdornItems.HELMET_CAUTION_SIGN.get())
            .add(AdornItems.RAILS_CAUTION_SIGN.get())
            .add(AdornItems.SURPRISE_CAUTION_SIGN.get());

        valueLookupBuilder(MoreConventionalItemTags.BOOKS)
            // Vanilla items
            .add(Items.BOOK, Items.ENCHANTED_BOOK, Items.WRITABLE_BOOK, Items.WRITTEN_BOOK)
            // Adorn items
            .add(AdornItems.GUIDE_BOOK.get(), AdornItems.TRADERS_MANUAL.get());

        valueLookupBuilder(AdornTags.BREWING_INPUTS)
            .add(AdornItems.MUG.get());
    }

    private void copy(AdornTags.TagPair tagPair) {
        copy(tagPair.block(), tagPair.item());
    }
}
