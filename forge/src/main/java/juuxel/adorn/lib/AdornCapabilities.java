package juuxel.adorn.lib;

import juuxel.adorn.block.AdornBlockEntities;
import juuxel.adorn.block.entity.BlockEntityWithFluidTank;
import juuxel.adorn.block.variant.BlockKind;
import juuxel.adorn.block.variant.BlockVariantSets;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.IBlockCapabilityProvider;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;
import net.neoforged.neoforge.transfer.item.WorldlyContainerWrapper;
import org.jspecify.annotations.Nullable;

public final class AdornCapabilities {
    private static final IBlockCapabilityProvider<ResourceHandler<ItemResource>, @Nullable Direction> INVENTORY_WRAPPER_FOR_BLOCK =
        (world, pos, state, blockEntity, side) -> blockEntity instanceof Container inventory ? getInventoryWrapper(inventory, side) : null;
    private static final ICapabilityProvider<BlockEntity, @Nullable Direction, ResourceHandler<ItemResource>> INVENTORY_WRAPPER_FOR_BLOCK_ENTITY =
        (blockEntity, side) -> blockEntity instanceof Container inventory ? getInventoryWrapper(inventory, side) : null;
    private static final IBlockCapabilityProvider<ResourceHandler<FluidResource>, @Nullable Direction> FLUID_TANK_FOR_BLOCK =
        (world, pos, state, blockEntity, side) -> blockEntity instanceof BlockEntityWithFluidTank withTank ? withTank.getTank() : null;
    private static final ICapabilityProvider<BlockEntity, @Nullable Direction, ResourceHandler<FluidResource>> FLUID_TANK_FOR_BLOCK_ENTITY =
        (blockEntity, side) -> blockEntity instanceof BlockEntityWithFluidTank withTank ? withTank.getTank() : null;

    public static void register(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.Item.BLOCK, AdornBlockEntities.BREWER.get(), INVENTORY_WRAPPER_FOR_BLOCK_ENTITY);

        var containerBlockKinds = new BlockKind[] {
            BlockKind.DRAWER,
            BlockKind.KITCHEN_CUPBOARD,
            BlockKind.SHELF,
        };

        for (var kind : containerBlockKinds) {
            for (var block : BlockVariantSets.get(kind)) {
                event.registerBlock(Capabilities.Item.BLOCK, INVENTORY_WRAPPER_FOR_BLOCK, block.get());
            }
        }

        event.registerBlockEntity(Capabilities.Fluid.BLOCK, AdornBlockEntities.BREWER.get(), FLUID_TANK_FOR_BLOCK_ENTITY);

        for (var kitchenSink : BlockVariantSets.get(BlockKind.KITCHEN_SINK)) {
            event.registerBlock(Capabilities.Fluid.BLOCK, FLUID_TANK_FOR_BLOCK, kitchenSink.get());
        }
    }

    private static ResourceHandler<ItemResource> getInventoryWrapper(Container inventory, @Nullable Direction side) {
        return side != null && inventory instanceof WorldlyContainer sided ? new WorldlyContainerWrapper(sided, side) : VanillaContainerWrapper.of(inventory);
    }
}
