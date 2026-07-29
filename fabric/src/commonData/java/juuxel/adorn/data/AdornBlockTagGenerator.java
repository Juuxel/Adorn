package juuxel.adorn.data;

import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.block.variant.BlockKind;
import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.BlockVariantSets;
import juuxel.adorn.lib.AdornTags;
import juuxel.adorn.lib.registry.RegisteredBlock;
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
        builder(AdornTags.PAINTED_PLANKS.block())
            .addAll(AdornBlocks.PAINTED_PLANKS.map(RegisteredBlock::key));
        builder(AdornTags.PAINTED_WOOD_SLABS.block())
            .addAll(AdornBlocks.PAINTED_WOOD_SLABS.map(RegisteredBlock::key));
        builder(AdornTags.PAINTED_WOOD_STAIRS.block())
            .addAll(AdornBlocks.PAINTED_WOOD_STAIRS.map(RegisteredBlock::key));
        builder(AdornTags.PAINTED_WOOD_FENCES.block())
            .addAll(AdornBlocks.PAINTED_WOOD_FENCES.map(RegisteredBlock::key));
        builder(AdornTags.PAINTED_WOOD_FENCE_GATES.block())
            .addAll(AdornBlocks.PAINTED_WOOD_FENCE_GATES.map(RegisteredBlock::key));
        builder(AdornTags.PAINTED_WOOD_PRESSURE_PLATES.block())
            .addAll(AdornBlocks.PAINTED_WOOD_PRESSURE_PLATES.map(RegisteredBlock::key));
        builder(AdornTags.PAINTED_WOOD_BUTTONS.block())
            .addAll(AdornBlocks.PAINTED_WOOD_BUTTONS.map(RegisteredBlock::key));
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
        builder(AdornTags.STANDING_CAUTION_SIGNS)
            .add(AdornBlocks.CAUTION_SIGN.key())
            .add(AdornBlocks.BEE_CAUTION_SIGN.key())
            .add(AdornBlocks.BOOK_CAUTION_SIGN.key())
            .add(AdornBlocks.CLIFF_CAUTION_SIGN.key())
            .add(AdornBlocks.FORBIDDEN_CAUTION_SIGN.key())
            .add(AdornBlocks.HELMET_CAUTION_SIGN.key())
            .add(AdornBlocks.RAILS_CAUTION_SIGN.key())
            .add(AdornBlocks.SURPRISE_CAUTION_SIGN.key());
        builder(AdornTags.WALL_CAUTION_SIGNS)
            .add(AdornBlocks.WALL_CAUTION_SIGN.key())
            .add(AdornBlocks.BEE_WALL_CAUTION_SIGN.key())
            .add(AdornBlocks.BOOK_WALL_CAUTION_SIGN.key())
            .add(AdornBlocks.CLIFF_WALL_CAUTION_SIGN.key())
            .add(AdornBlocks.FORBIDDEN_WALL_CAUTION_SIGN.key())
            .add(AdornBlocks.HELMET_WALL_CAUTION_SIGN.key())
            .add(AdornBlocks.RAILS_WALL_CAUTION_SIGN.key())
            .add(AdornBlocks.SURPRISE_WALL_CAUTION_SIGN.key());
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
        builder(AdornTags.SHELVES.block())
            .forceAddTag(AdornTags.WOODEN_SHELVES.block())
            .add(BlockVariantSets.get(BlockKind.SHELF, BlockVariant.IRON).key());
        builder(AdornTags.CANDLELIT_LANTERNS.block())
            .add(AdornBlocks.CANDLELIT_LANTERN.key())
            .forceAddTag(AdornTags.DYED_CANDLELIT_LANTERNS.block());
        builder(AdornTags.COPPER_PIPES.block())
            .addAll(AdornBlocks.COPPER_PIPES.map(RegisteredBlock::key));
        builder(AdornTags.KITCHEN_BLOCKS.block())
            .forceAddTag(AdornTags.KITCHEN_COUNTERS.block())
            .forceAddTag(AdornTags.KITCHEN_CUPBOARDS.block())
            .forceAddTag(AdornTags.KITCHEN_SINKS.block());
        builder(AdornTags.REGULAR_CHIMNEYS.block())
            .add(AdornBlocks.BRICK_CHIMNEY.key())
            .add(AdornBlocks.STONE_BRICK_CHIMNEY.key())
            .add(AdornBlocks.NETHER_BRICK_CHIMNEY.key())
            .add(AdornBlocks.RED_NETHER_BRICK_CHIMNEY.key())
            .add(AdornBlocks.COBBLESTONE_CHIMNEY.key());
        builder(AdornTags.PRISMARINE_CHIMNEYS.block())
            .add(AdornBlocks.PRISMARINE_CHIMNEY.key())
            .add(AdornBlocks.MAGMATIC_PRISMARINE_CHIMNEY.key())
            .add(AdornBlocks.SOULFUL_PRISMARINE_CHIMNEY.key());
        builder(AdornTags.CHIMNEYS.block())
            .addTag(AdornTags.REGULAR_CHIMNEYS.block())
            .addTag(AdornTags.PRISMARINE_CHIMNEYS.block());
        builder(AdornTags.COPPER_PIPES_CONNECT_TO)
            .addTag(AdornTags.COPPER_PIPES.block());
        builder(AdornTags.FILLED_CRATES.block())
            .add(AdornBlocks.APPLE_CRATE.key())
            .add(AdornBlocks.WHEAT_CRATE.key())
            .add(AdornBlocks.CARROT_CRATE.key())
            .add(AdornBlocks.POTATO_CRATE.key())
            .add(AdornBlocks.MELON_CRATE.key())
            .add(AdornBlocks.WHEAT_SEED_CRATE.key())
            .add(AdornBlocks.MELON_SEED_CRATE.key())
            .add(AdornBlocks.PUMPKIN_SEED_CRATE.key())
            .add(AdornBlocks.BEETROOT_CRATE.key())
            .add(AdornBlocks.BEETROOT_SEED_CRATE.key())
            .add(AdornBlocks.SWEET_BERRY_CRATE.key())
            .add(AdornBlocks.COCOA_BEAN_CRATE.key())
            .add(AdornBlocks.NETHER_WART_CRATE.key())
            .add(AdornBlocks.SUGAR_CANE_CRATE.key())
            .add(AdornBlocks.EGG_CRATE.key())
            .add(AdornBlocks.HONEYCOMB_CRATE.key())
            .add(AdornBlocks.LIL_TATER_CRATE.key());
        builder(AdornTags.CRATES.block())
            .add(AdornBlocks.CRATE.key())
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

        builder(BlockTags.CLIMBABLE)
            .add(AdornBlocks.CHAIN_LINK_FENCE.key())
            .add(AdornBlocks.STONE_LADDER.key());

        builder(BlockTags.MINEABLE_WITH_AXE)
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
            .add(AdornBlocks.PICKET_FENCE.key())
            .add(AdornBlocks.TRADING_STATION.key());

        builder(BlockTags.MINEABLE_WITH_PICKAXE)
            .addTag(AdornTags.CHIMNEYS.block())
            .forceAddTag(AdornTags.STONE_PLATFORMS.block())
            .forceAddTag(AdornTags.STONE_POSTS.block())
            .forceAddTag(AdornTags.STONE_STEPS.block())
            .forceAddTag(AdornTags.TABLE_LAMPS.block())
            .addTag(AdornTags.CANDLELIT_LANTERNS.block())
            .addTag(AdornTags.COPPER_PIPES.block())
            .addTag(AdornTags.CAUTION_SIGNS.block())
            .add(AdornBlocks.CHAIN_LINK_FENCE.key())
            .add(BlockVariantSets.get(BlockKind.SHELF, BlockVariant.IRON).key())
            .add(AdornBlocks.STONE_LADDER.key())
            .add(AdornBlocks.BREWER.key())
            .add(AdornBlocks.BARRICADE.key());
    }

    private void addConventionalTags() {
        builder(MoreConventionalBlockTags.LANTERNS)
            .addTag(AdornTags.CANDLELIT_LANTERNS.block());
    }

    private void addPaintedVariants(TagKey<Block> tag, BlockKind kind) {
        var builder = builder(tag);

        for (BlockVariant variant : BlockVariant.PAINTED_WOODS.values()) {
            var block = BlockVariantSets.get(kind, variant);
            if (block != null) builder.add(block.key());
        }
    }
}
