package juuxel.adorn.block

import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

interface SneakClickHandler {
    fun onSneakClick(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity): ActionResult
}
