package juuxel.adorn.platform.forge.block

import juuxel.adorn.api.block.BlockVariant
import juuxel.adorn.block.SofaBlock
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockView
import net.minecraft.world.WorldView

class SofaBlockForge(variant: BlockVariant) : SofaBlock(variant) {
    override fun isBed(state: BlockState, world: BlockView, pos: BlockPos, player: Entity?): Boolean = true

    override fun getBedDirection(state: BlockState, world: WorldView, pos: BlockPos): Direction? =
        getSleepingDirection(world, pos)?.opposite
}
