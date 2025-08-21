package juuxel.adorn.data;

import juuxel.adorn.block.AdornBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public final class AdornBlockLootTableGenerator extends FabricBlockLootTableProvider {
    public AdornBlockLootTableGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        AdornBlocks.PAINTED_PLANKS.values().forEach(this::addDrop);
        AdornBlocks.PAINTED_WOOD_SLABS.values().forEach(block -> addDrop(block, this::slabDrops));
        AdornBlocks.PAINTED_WOOD_STAIRS.values().forEach(this::addDrop);
        AdornBlocks.PAINTED_WOOD_FENCES.values().forEach(this::addDrop);
        AdornBlocks.PAINTED_WOOD_FENCE_GATES.values().forEach(this::addDrop);
        AdornBlocks.PAINTED_WOOD_PRESSURE_PLATES.values().forEach(this::addDrop);
        AdornBlocks.PAINTED_WOOD_BUTTONS.values().forEach(this::addDrop);
        addDrop(AdornBlocks.BARRICADE.get());
    }
}
