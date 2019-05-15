package juuxel.adorn.block

import juuxel.adorn.lib.ModEntities
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.SpawnType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.state.StateFactory
import net.minecraft.state.property.Properties
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class SeatBlock(settings: Settings) : Block(settings) {
    init {
        defaultState = defaultState.with(OCCUPIED, false)
    }

    open val sittingYOffset: Double = 0.0

    override fun activate(
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hitResult: BlockHitResult
    ): Boolean {
        val occupied = state[OCCUPIED]
        return if (world.isClient) {
            !occupied
        } else if (!world.isClient && !state[OCCUPIED]) {
            val entity = ModEntities.SITTING_VEHICLE.spawn(world, null, null, player, pos, SpawnType.TRIGGERED, false, false)
            entity?.setPos(pos, sittingYOffset)
            world.setBlockState(pos, state.with(OCCUPIED, true))
            player.startRiding(entity, true)
            true
        } else false
    }

    override fun appendProperties(builder: StateFactory.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(OCCUPIED)
    }

    companion object {
        @JvmField val OCCUPIED = Properties.OCCUPIED
    }
}
