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
        builder(AdornTags.PLATFORMS.block())
            .forceAddTag(AdornTags.WOODEN_PLATFORMS.block())
            .forceAddTag(AdornTags.STONE_PLATFORMS.block());
        builder(AdornTags.POSTS.block())
            .forceAddTag(AdornTags.WOODEN_POSTS.block())
            .forceAddTag(AdornTags.STONE_POSTS.block());
        builder(AdornTags.STEPS.block())
            .forceAddTag(AdornTags.WOODEN_STEPS.block())
            .forceAddTag(AdornTags.STONE_STEPS.block());
        valueLookupBuilder(AdornTags.SHELVES.block())
            .forceAddTag(AdornTags.WOODEN_SHELVES.block())
            .add(BlockVariantSets.get(BlockKind.SHELF, BlockVariant.IRON).get());
        valueLookupBuilder(AdornTags.CANDLELIT_LANTERNS.block())
            .add(AdornBlocks.CANDLELIT_LANTERN.get())
            .forceAddTag(AdornTags.DYED_CANDLELIT_LANTERNS.block());
        valueLookupBuilder(AdornTags.COPPER_PIPES.block())
            .add(AdornBlocks.COPPER_PIPE.get())
            .add(AdornBlocks.EXPOSED_COPPER_PIPE.get())
            .add(AdornBlocks.WEATHERED_COPPER_PIPE.get())
            .add(AdornBlocks.OXIDIZED_COPPER_PIPE.get())
            .add(AdornBlocks.WAXED_COPPER_PIPE.get())
            .add(AdornBlocks.WAXED_EXPOSED_COPPER_PIPE.get())
            .add(AdornBlocks.WAXED_WEATHERED_COPPER_PIPE.get())
            .add(AdornBlocks.WAXED_OXIDIZED_COPPER_PIPE.get());
        builder(AdornTags.KITCHEN_BLOCKS.block())
            .forceAddTag(AdornTags.KITCHEN_COUNTERS.block())
            .forceAddTag(AdornTags.KITCHEN_CUPBOARDS.block())
            .forceAddTag(AdornTags.KITCHEN_SINKS.block());
        valueLookupBuilder(AdornTags.REGULAR_CHIMNEYS.block())
            .add(AdornBlocks.BRICK_CHIMNEY.get())
            .add(AdornBlocks.STONE_BRICK_CHIMNEY.get())
            .add(AdornBlocks.NETHER_BRICK_CHIMNEY.get())
            .add(AdornBlocks.RED_NETHER_BRICK_CHIMNEY.get())
            .add(AdornBlocks.COBBLESTONE_CHIMNEY.get());
        valueLookupBuilder(AdornTags.PRISMARINE_CHIMNEYS.block())
            .add(AdornBlocks.PRISMARINE_CHIMNEY.get())
            .add(AdornBlocks.MAGMATIC_PRISMARINE_CHIMNEY.get())
            .add(AdornBlocks.SOULFUL_PRISMARINE_CHIMNEY.get());
        builder(AdornTags.CHIMNEYS.block())
            .addTag(AdornTags.REGULAR_CHIMNEYS.block())
            .addTag(AdornTags.PRISMARINE_CHIMNEYS.block());
        builder(AdornTags.COPPER_PIPES_CONNECT_TO)
            .addTag(AdornTags.COPPER_PIPES.block());
        valueLookupBuilder(AdornTags.FILLED_CRATES.block())
            .add(AdornBlocks.APPLE_CRATE.get())
            .add(AdornBlocks.WHEAT_CRATE.get())
            .add(AdornBlocks.CARROT_CRATE.get())
            .add(AdornBlocks.POTATO_CRATE.get())
            .add(AdornBlocks.MELON_CRATE.get())
            .add(AdornBlocks.WHEAT_SEED_CRATE.get())
            .add(AdornBlocks.MELON_SEED_CRATE.get())
            .add(AdornBlocks.PUMPKIN_SEED_CRATE.get())
            .add(AdornBlocks.BEETROOT_CRATE.get())
            .add(AdornBlocks.BEETROOT_SEED_CRATE.get())
            .add(AdornBlocks.SWEET_BERRY_CRATE.get())
            .add(AdornBlocks.COCOA_BEAN_CRATE.get())
            .add(AdornBlocks.NETHER_WART_CRATE.get())
            .add(AdornBlocks.SUGAR_CANE_CRATE.get())
            .add(AdornBlocks.EGG_CRATE.get())
            .add(AdornBlocks.HONEYCOMB_CRATE.get())
            .add(AdornBlocks.LIL_TATER_CRATE.get());
        valueLookupBuilder(AdornTags.CRATES.block())
            .add(AdornBlocks.CRATE.get())
            .addTag(AdornTags.FILLED_CRATES.block());

        addVanillaTags();
        addConventionalTags();
    }

    private void addVanillaTags() {
        builder(BlockTags.PLANKS).addTag(AdornTags.PAINTED_PLANKS.block());
        builder(BlockTags.WOODEN_SLABS).addTag(AdornTags.PAINTED_WOOD_SLABS.block());
        builder(BlockTags.WOODEN_STAIRS).addTag(AdornTags.PAINTED_WOOD_STAIRS.block());
        builder(BlockTags.WOODEN_FENCES).addTag(AdornTags.PAINTED_WOOD_FENCES.block());
        builder(BlockTags.FENCE_GATES).addTag(AdornTags.PAINTED_WOOD_FENCE_GATES.block());
        builder(BlockTags.WOODEN_PRESSURE_PLATES).addTag(AdornTags.PAINTED_WOOD_PRESSURE_PLATES.block());
        builder(BlockTags.WOODEN_BUTTONS).addTag(AdornTags.PAINTED_WOOD_BUTTONS.block());

        valueLookupBuilder(BlockTags.CLIMBABLE)
            .add(AdornBlocks.CHAIN_LINK_FENCE.get())
            .add(AdornBlocks.STONE_LADDER.get());

        valueLookupBuilder(BlockTags.MINEABLE_WITH_AXE)
            .forceAddTag(AdornTags.CHAIRS.block())
            .forceAddTag(AdornTags.TABLES.block())
            .addTag(AdornTags.KITCHEN_BLOCKS.block())
            .forceAddTag(AdornTags.DRAWERS.block())
            .forceAddTag(AdornTags.WOODEN_POSTS.block())
            .forceAddTag(AdornTags.WOODEN_PLATFORMS.block())
            .forceAddTag(AdornTags.WOODEN_STEPS.block())
            .forceAddTag(AdornTags.WOODEN_SHELVES.block())
            .forceAddTag(AdornTags.COFFEE_TABLES.block())
            .forceAddTag(AdornTags.BENCHES.block())
            .addTag(AdornTags.CRATES.block())
            .forceAddTag(AdornTags.SOFAS.block())
            .add(AdornBlocks.PICKET_FENCE.get())
            .add(AdornBlocks.TRADING_STATION.get());

        valueLookupBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
            .addTag(AdornTags.CHIMNEYS.block())
            .forceAddTag(AdornTags.STONE_PLATFORMS.block())
            .forceAddTag(AdornTags.STONE_POSTS.block())
            .forceAddTag(AdornTags.STONE_STEPS.block())
            .forceAddTag(AdornTags.TABLE_LAMPS.block())
            .addTag(AdornTags.CANDLELIT_LANTERNS.block())
            .addTag(AdornTags.COPPER_PIPES.block())
            .addTag(AdornTags.CAUTION_SIGNS.block())
            .add(AdornBlocks.CHAIN_LINK_FENCE.get())
            .add(BlockVariantSets.get(BlockKind.SHELF, BlockVariant.IRON).get())
            .add(AdornBlocks.STONE_LADDER.get())
            .add(AdornBlocks.BREWER.get())
            .add(AdornBlocks.BARRICADE.get());
    }

    private void addConventionalTags() {
        builder(MoreConventionalBlockTags.LANTERNS)
            .addTag(AdornTags.CANDLELIT_LANTERNS.block());
    }

    private void addPaintedVariants(TagKey<Block> tag, BlockKind kind) {
        var builder = valueLookupBuilder(tag);

        for (BlockVariant variant : BlockVariant.PAINTED_WOODS.values()) {
            var block = BlockVariantSets.get(kind, variant);
            if (block != null) builder.add(block.get());
        }
    }
}
