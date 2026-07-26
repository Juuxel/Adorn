package juuxel.adorn.data;

import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.block.variant.BlockKind;
import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.BlockVariantSets;
import juuxel.adorn.lib.AdornTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public final class AdornBlockTagGenerator extends FabricTagsProvider.BlockTagsProvider {
    public AdornBlockTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        valueLookupBuilder(AdornTags.PAINTED_PLANKS.block())
            .add(AdornBlocks.PAINTED_PLANKS.values().toArray(Block[]::new));
        valueLookupBuilder(AdornTags.PAINTED_WOOD_SLABS.block())
            .add(AdornBlocks.PAINTED_WOOD_SLABS.values().toArray(Block[]::new));
        valueLookupBuilder(AdornTags.PAINTED_WOOD_STAIRS.block())
            .add(AdornBlocks.PAINTED_WOOD_STAIRS.values().toArray(Block[]::new));
        valueLookupBuilder(AdornTags.PAINTED_WOOD_FENCES.block())
            .add(AdornBlocks.PAINTED_WOOD_FENCES.values().toArray(Block[]::new));
        valueLookupBuilder(AdornTags.PAINTED_WOOD_FENCE_GATES.block())
            .add(AdornBlocks.PAINTED_WOOD_FENCE_GATES.values().toArray(Block[]::new));
        valueLookupBuilder(AdornTags.PAINTED_WOOD_PRESSURE_PLATES.block())
            .add(AdornBlocks.PAINTED_WOOD_PRESSURE_PLATES.values().toArray(Block[]::new));
        valueLookupBuilder(AdornTags.PAINTED_WOOD_BUTTONS.block())
            .add(AdornBlocks.PAINTED_WOOD_BUTTONS.values().toArray(Block[]::new));
        addPaintedVariants(AdornTags.PAINTED_CHAIRS.block(), BlockKind.CHAIR);
        addPaintedVariants(AdornTags.PAINTED_TABLES.block(), BlockKind.TABLE);
        addPaintedVariants(AdornTags.PAINTED_DRAWERS.block(), BlockKind.DRAWER);
        addPaintedVariants(AdornTags.PAINTED_BENCHES.block(), BlockKind.BENCH);
        addPaintedVariants(AdornTags.PAINTED_KITCHEN_COUNTERS.block(), BlockKind.KITCHEN_COUNTER);
        addPaintedVariants(AdornTags.PAINTED_KITCHEN_CUPBOARDS.block(), BlockKind.KITCHEN_CUPBOARD);
        addPaintedVariants(AdornTags.PAINTED_KITCHEN_SINKS.block(), BlockKind.KITCHEN_SINK);
        addPaintedVariants(AdornTags.PAINTED_WOOD_POSTS.block(), BlockKind.POST);
        addPaintedVariants(AdornTags.PAINTED_WOOD_PLATFORMS.block(), BlockKind.PLATFORM);
        addPaintedVariants(AdornTags.PAINTED_WOOD_STEPS.block(), BlockKind.STEP);
        addPaintedVariants(AdornTags.PAINTED_WOOD_SHELVES.block(), BlockKind.SHELF);
        addPaintedVariants(AdornTags.PAINTED_COFFEE_TABLES.block(), BlockKind.COFFEE_TABLE);
        builder(BlockTags.PLANKS).addTag(AdornTags.PAINTED_PLANKS.block());
        builder(BlockTags.WOODEN_SLABS).addTag(AdornTags.PAINTED_WOOD_SLABS.block());
        builder(BlockTags.WOODEN_STAIRS).addTag(AdornTags.PAINTED_WOOD_STAIRS.block());
        builder(BlockTags.WOODEN_FENCES).addTag(AdornTags.PAINTED_WOOD_FENCES.block());
        builder(BlockTags.FENCE_GATES).addTag(AdornTags.PAINTED_WOOD_FENCE_GATES.block());
        builder(BlockTags.WOODEN_PRESSURE_PLATES).addTag(AdornTags.PAINTED_WOOD_PRESSURE_PLATES.block());
        builder(BlockTags.WOODEN_BUTTONS).addTag(AdornTags.PAINTED_WOOD_BUTTONS.block());
        valueLookupBuilder(AdornTags.STANDING_CAUTION_SIGNS)
            .add(AdornBlocks.CAUTION_SIGN.get())
            .add(AdornBlocks.BEE_CAUTION_SIGN.get())
            .add(AdornBlocks.BOOK_CAUTION_SIGN.get())
            .add(AdornBlocks.CLIFF_CAUTION_SIGN.get())
            .add(AdornBlocks.FORBIDDEN_CAUTION_SIGN.get())
            .add(AdornBlocks.HELMET_CAUTION_SIGN.get())
            .add(AdornBlocks.RAILS_CAUTION_SIGN.get())
            .add(AdornBlocks.SURPRISE_CAUTION_SIGN.get());
        valueLookupBuilder(AdornTags.WALL_CAUTION_SIGNS)
            .add(AdornBlocks.WALL_CAUTION_SIGN.get())
            .add(AdornBlocks.BEE_WALL_CAUTION_SIGN.get())
            .add(AdornBlocks.BOOK_WALL_CAUTION_SIGN.get())
            .add(AdornBlocks.CLIFF_WALL_CAUTION_SIGN.get())
            .add(AdornBlocks.FORBIDDEN_WALL_CAUTION_SIGN.get())
            .add(AdornBlocks.HELMET_WALL_CAUTION_SIGN.get())
            .add(AdornBlocks.RAILS_WALL_CAUTION_SIGN.get())
            .add(AdornBlocks.SURPRISE_WALL_CAUTION_SIGN.get());
        builder(AdornTags.CAUTION_SIGNS.block())
            .addTag(AdornTags.STANDING_CAUTION_SIGNS)
            .addTag(AdornTags.WALL_CAUTION_SIGNS);
    }

    private void addPaintedVariants(TagKey<Block> tag, BlockKind kind) {
        var builder = valueLookupBuilder(tag);

        for (BlockVariant variant : BlockVariant.PAINTED_WOODS.values()) {
            var block = BlockVariantSets.get(kind, variant);
            if (block != null) builder.add(block.get());
        }
    }
}
