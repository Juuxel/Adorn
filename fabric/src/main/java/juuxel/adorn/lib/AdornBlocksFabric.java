package juuxel.adorn.lib;

import juuxel.adorn.CommonEventHandlers;
import juuxel.adorn.block.AdornBlockEntities;
import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.block.SneakClickHandler;
import juuxel.adorn.block.entity.BrewerBlockEntityFabric;
import juuxel.adorn.block.entity.KitchenSinkBlockEntityFabric;
import juuxel.adorn.block.variant.BlockKind;
import juuxel.adorn.block.variant.BlockVariantSets;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.WeatheringCopperBlocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class AdornBlocksFabric {
    public static void init() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            var state = world.getBlockState(hitResult.getBlockPos());
            // Check that:
            // - the block is a sneak-click handler
            // - the player is sneaking
            // - the player isn't holding an item (for block item and bucket support)
            if (state.getBlock() instanceof SneakClickHandler handler && player.isShiftKeyDown() && player.getItemInHand(hand).isEmpty()) {
                return handler.onSneakClick(state, world, hitResult.getBlockPos(), player, hand, hitResult);
            } else {
                return InteractionResult.PASS;
            }
        });

        UseBlockCallback.EVENT.register(CommonEventHandlers::handleCarpets);
        FluidStorage.SIDED.registerForBlockEntity(
            (brewer, side) -> ((BrewerBlockEntityFabric) brewer).getFluidStorage(),
            AdornBlockEntities.BREWER.get()
        );

        var copperPipeBlocks = new WeatheringCopperBlocks(
            AdornBlocks.COPPER_PIPE.get(),
            AdornBlocks.EXPOSED_COPPER_PIPE.get(),
            AdornBlocks.WEATHERED_COPPER_PIPE.get(),
            AdornBlocks.OXIDIZED_COPPER_PIPE.get(),
            AdornBlocks.WAXED_COPPER_PIPE.get(),
            AdornBlocks.WAXED_EXPOSED_COPPER_PIPE.get(),
            AdornBlocks.WAXED_WEATHERED_COPPER_PIPE.get(),
            AdornBlocks.WAXED_OXIDIZED_COPPER_PIPE.get()
        );
        OxidizableBlocksRegistry.registerWeatheringCopperBlocks(copperPipeBlocks);

        FlammableBlockRegistry.getDefaultInstance().add(AdornTags.PAINTED_PLANKS.block(), 5, 20);
        FlammableBlockRegistry.getDefaultInstance().add(AdornTags.PAINTED_WOOD_SLABS.block(), 5, 20);
        FlammableBlockRegistry.getDefaultInstance().add(AdornTags.PAINTED_WOOD_STAIRS.block(), 5, 20);
        FlammableBlockRegistry.getDefaultInstance().add(AdornTags.PAINTED_WOOD_FENCES.block(), 5, 20);
        FlammableBlockRegistry.getDefaultInstance().add(AdornTags.PAINTED_WOOD_FENCE_GATES.block(), 5, 20);
    }

    public static void afterRegister() {
        for (var kitchenSink : BlockVariantSets.get(BlockKind.KITCHEN_SINK)) {
            FluidStorage.SIDED.registerForBlocks(KitchenSinkBlockEntityFabric.FLUID_STORAGE_PROVIDER, kitchenSink.get());
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends BlockEntity, U extends T> BlockEntityType<U> forceType(BlockEntityType<T> type) {
        return (BlockEntityType<U>) type;
    }
}
