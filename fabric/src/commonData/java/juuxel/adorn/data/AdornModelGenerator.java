package juuxel.adorn.data;

import juuxel.adorn.block.AdornBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;

public final class AdornModelGenerator extends FabricModelProvider {
    public AdornModelGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {
        AdornBlocks.PAINTED_PLANKS.forEach((color, planks) -> {
            generator.registerCubeAllModelTexturePool(planks)
                .slab(AdornBlocks.PAINTED_WOOD_SLABS.getEager(color))
                .stairs(AdornBlocks.PAINTED_WOOD_STAIRS.getEager(color))
                .fence(AdornBlocks.PAINTED_WOOD_FENCES.getEager(color))
                .fenceGate(AdornBlocks.PAINTED_WOOD_FENCE_GATES.getEager(color))
                .pressurePlate(AdornBlocks.PAINTED_WOOD_PRESSURE_PLATES.getEager(color))
                .button(AdornBlocks.PAINTED_WOOD_BUTTONS.getEager(color));
        });
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
    }
}
