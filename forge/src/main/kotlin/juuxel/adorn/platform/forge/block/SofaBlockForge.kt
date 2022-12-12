package juuxel.adorn.platform.forge.block

import juuxel.adorn.block.SofaBlock
import juuxel.adorn.block.variant.BlockVariant
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldView

class SofaBlockForge(variant: BlockVariant) : SofaBlock(variant) {
    override fun isBed(state: BlockState, world: BlockView, pos: BlockPos, player: Entity?): Boolean = true

    override fun getBedDirection(state: BlockState, world: WorldView, pos: BlockPos): Direction? =
        getSleepingDirection(world, pos)?.opposite

    override fun setBedOccupied(state: BlockState, world: World, pos: BlockPos, entity: LivingEntity, value: Boolean) {
        super.setBedOccupied(state, world, pos, entity, value)
        val neighborPos = pos.offset(getSleepingDirection(world, pos, ignoreNeighbors = true))
        world.setBlockState(neighborPos, world.getBlockState(neighborPos).with(OCCUPIED, value))
    }
}
