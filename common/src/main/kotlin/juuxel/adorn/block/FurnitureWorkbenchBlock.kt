@file:Suppress("OVERRIDE_DEPRECATION")

package juuxel.adorn.block

import juuxel.adorn.block.entity.FurnitureWorkbenchBlockEntity
import juuxel.adorn.platform.PlatformBridges
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class FurnitureWorkbenchBlock(settings: Settings) : VisibleBlockWithEntity(settings) {
    override fun onUse(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hit: BlockHitResult): ActionResult {
        if (world.isClient) return ActionResult.SUCCESS

        val blockEntity = world.getBlockEntity(pos)
        if (blockEntity is FurnitureWorkbenchBlockEntity) {
            PlatformBridges.menus.open(player, blockEntity, pos)
        }

        return ActionResult.CONSUME
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity =
        FurnitureWorkbenchBlockEntity(pos, state)
}
