package juuxel.adorn.lib

import juuxel.adorn.CommonEventHandlers
import juuxel.adorn.block.AdornBlockEntities
import juuxel.adorn.block.AdornBlocks
import juuxel.adorn.block.SneakClickHandler
import juuxel.adorn.block.entity.BrewerBlockEntityFabric
import juuxel.adorn.block.entity.KitchenSinkBlockEntityFabric
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage
import net.minecraft.util.ActionResult

object AdornBlocksFabric {
    fun init() {
        UseBlockCallback.EVENT.register(
            UseBlockCallback { player, world, hand, hitResult ->
                val state = world.getBlockState(hitResult.blockPos)
                val block = state.block
                // Check that:
                // - the block is a sneak-click handler
                // - the player is sneaking
                // - the player isn't holding an item (for block item and bucket support)
                if (block is SneakClickHandler && player.isSneaking && player.getStackInHand(hand).isEmpty) {
                    block.onSneakClick(state, world, hitResult.blockPos, player, hand, hitResult)
                } else ActionResult.PASS
            }
        )

        UseBlockCallback.EVENT.register(CommonEventHandlers::handleCarpets)
        FluidStorage.SIDED.registerForBlocks(
            KitchenSinkBlockEntityFabric.FLUID_STORAGE_PROVIDER,
            AdornBlocks.OAK_KITCHEN_SINK,
            AdornBlocks.SPRUCE_KITCHEN_SINK,
            AdornBlocks.BIRCH_KITCHEN_SINK,
            AdornBlocks.JUNGLE_KITCHEN_SINK,
            AdornBlocks.ACACIA_KITCHEN_SINK,
            AdornBlocks.DARK_OAK_KITCHEN_SINK,
            AdornBlocks.MANGROVE_KITCHEN_SINK,
            AdornBlocks.CRIMSON_KITCHEN_SINK,
            AdornBlocks.WARPED_KITCHEN_SINK,
        )
        FluidStorage.SIDED.registerForBlockEntity(
            { brewer, _ -> (brewer as BrewerBlockEntityFabric).fluidStorage },
            AdornBlockEntities.BREWER
        )
    }
}
