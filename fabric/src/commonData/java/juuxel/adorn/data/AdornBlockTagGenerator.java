package juuxel.adorn.data;

import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.block.variant.BlockKind;
import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.BlockVariantSets;
import juuxel.adorn.lib.AdornTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;

import java.util.concurrent.CompletableFuture;

public final class AdornBlockTagGenerator extends FabricTagProvider.BlockTagProvider {
    public AdornBlockTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(AdornTags.PAINTED_PLANKS.block())
            .add(AdornBlocks.PAINTED_PLANKS.values().toArray(Block[]::new));
        getOrCreateTagBuilder(AdornTags.PAINTED_WOOD_SLABS.block())
            .add(AdornBlocks.PAINTED_WOOD_SLABS.values().toArray(Block[]::new));
        getOrCreateTagBuilder(AdornTags.PAINTED_WOOD_STAIRS.block())
            .add(AdornBlocks.PAINTED_WOOD_STAIRS.values().toArray(Block[]::new));
        getOrCreateTagBuilder(AdornTags.PAINTED_WOOD_FENCES.block())
            .add(AdornBlocks.PAINTED_WOOD_FENCES.values().toArray(Block[]::new));
        getOrCreateTagBuilder(AdornTags.PAINTED_WOOD_FENCE_GATES.block())
            .add(AdornBlocks.PAINTED_WOOD_FENCE_GATES.values().toArray(Block[]::new));
        getOrCreateTagBuilder(AdornTags.PAINTED_WOOD_PRESSURE_PLATES.block())
            .add(AdornBlocks.PAINTED_WOOD_PRESSURE_PLATES.values().toArray(Block[]::new));
        getOrCreateTagBuilder(AdornTags.PAINTED_WOOD_BUTTONS.block())
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
        getOrCreateTagBuilder(BlockTags.PLANKS).addTag(AdornTags.PAINTED_PLANKS.block());
        getOrCreateTagBuilder(BlockTags.WOODEN_SLABS).addTag(AdornTags.PAINTED_WOOD_SLABS.block());
        getOrCreateTagBuilder(BlockTags.WOODEN_STAIRS).addTag(AdornTags.PAINTED_WOOD_STAIRS.block());
        getOrCreateTagBuilder(BlockTags.WOODEN_FENCES).addTag(AdornTags.PAINTED_WOOD_FENCES.block());
        getOrCreateTagBuilder(BlockTags.FENCE_GATES).addTag(AdornTags.PAINTED_WOOD_FENCE_GATES.block());
        getOrCreateTagBuilder(BlockTags.WOODEN_PRESSURE_PLATES).addTag(AdornTags.PAINTED_WOOD_PRESSURE_PLATES.block());
        getOrCreateTagBuilder(BlockTags.WOODEN_BUTTONS).addTag(AdornTags.PAINTED_WOOD_BUTTONS.block());
    }

    private void addPaintedVariants(TagKey<Block> tag, BlockKind kind) {
        var builder = getOrCreateTagBuilder(tag);

        for (BlockVariant variant : BlockVariant.PAINTED_WOODS.values()) {
            var block = BlockVariantSets.get(kind, variant);
            if (block != null) builder.add(block.get());
        }
    }
}
