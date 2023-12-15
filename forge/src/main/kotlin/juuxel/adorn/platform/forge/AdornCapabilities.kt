package juuxel.adorn.platform.forge

import juuxel.adorn.block.AdornBlockEntities
import juuxel.adorn.block.variant.BlockKind
import juuxel.adorn.block.variant.BlockVariantSets
import juuxel.adorn.platform.forge.block.entity.BlockEntityWithFluidTank
import net.minecraft.block.entity.BlockEntity
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SidedInventory
import net.minecraft.util.math.Direction
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.IBlockCapabilityProvider
import net.neoforged.neoforge.capabilities.ICapabilityProvider
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.wrapper.InvWrapper
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper

object AdornCapabilities {
    private val INVENTORY_WRAPPER_FOR_BLOCK: IBlockCapabilityProvider<IItemHandler, Direction?> =
        IBlockCapabilityProvider { _, _, _, blockEntity, side ->
            if (blockEntity is Inventory) {
                getInventoryWrapper(blockEntity, side)
            } else {
                null
            }
        }

    private val INVENTORY_WRAPPER_FOR_BLOCK_ENTITY: ICapabilityProvider<BlockEntity, Direction?, IItemHandler> =
        ICapabilityProvider { blockEntity, side ->
            if (blockEntity is Inventory) {
                getInventoryWrapper(blockEntity, side)
            } else {
                null
            }
        }

    private val FLUID_TANK_FOR_BLOCK: IBlockCapabilityProvider<IFluidHandler, Direction?> =
        IBlockCapabilityProvider { _, _, _, blockEntity, side ->
            if (blockEntity is BlockEntityWithFluidTank) {
                blockEntity.tank
            } else {
                null
            }
        }

    private val FLUID_TANK_FOR_BLOCK_ENTITY: ICapabilityProvider<BlockEntity, Direction?, IFluidHandler> =
        ICapabilityProvider { blockEntity, _ ->
            if (blockEntity is BlockEntityWithFluidTank) {
                blockEntity.tank
            } else {
                null
            }
        }

    @SubscribeEvent
    fun register(event: RegisterCapabilitiesEvent) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, AdornBlockEntities.BREWER, INVENTORY_WRAPPER_FOR_BLOCK_ENTITY)

        val containerBlockKinds = listOf(
            BlockKind.DRAWER,
            BlockKind.KITCHEN_CUPBOARD,
            BlockKind.SHELF,
        )

        for (kind in containerBlockKinds) {
            for (block in BlockVariantSets.get(kind)) {
                event.registerBlock(Capabilities.ItemHandler.BLOCK, INVENTORY_WRAPPER_FOR_BLOCK, block.get())
            }
        }

        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, AdornBlockEntities.BREWER, FLUID_TANK_FOR_BLOCK_ENTITY)

        for (kitchenSink in BlockVariantSets.get(BlockKind.KITCHEN_SINK)) {
            event.registerBlock(Capabilities.FluidHandler.BLOCK, FLUID_TANK_FOR_BLOCK, kitchenSink.get())
        }
    }

    private fun getInventoryWrapper(inventory: Inventory, side: Direction?): IItemHandler =
        if (side != null && inventory is SidedInventory) {
            SidedInvWrapper(inventory, side)
        } else {
            InvWrapper(inventory)
        }
}
