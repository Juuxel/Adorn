@file:Suppress("DEPRECATION")
package juuxel.adorn.block

import com.google.common.base.Predicates
import juuxel.adorn.criterion.AdornCriteria
import juuxel.adorn.entity.AdornEntities
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.world.World

abstract class SeatBlock(settings: Settings) : Block(settings) {
    init {
        if (@Suppress("LeakingThis") isSittingEnabled())
            defaultState = defaultState.with(OCCUPIED, false)
    }

    open val sittingYOffset: Double = 0.0
    abstract val sittingStat: Identifier?

    override fun onUse(
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hit: BlockHitResult
    ): ActionResult {
        if (!isSittingEnabled())
            return super.onUse(state, world, pos, player, hand, hit)

        val actualPos = getActualSeatPos(world, state, pos)
        val actualState = if (pos == actualPos) state else world.getBlockState(actualPos)

        if (state !== actualState && actualState.block !is SeatBlock)
            return ActionResult.PASS

        val occupied = actualState[OCCUPIED]
        return if (!occupied) {
            if (world is ServerWorld) {
                val entity = AdornEntities.SEAT.create(world)
                entity?.setPos(actualPos, sittingYOffset)
                world.spawnEntity(entity)
                world.setBlockState(actualPos, actualState.with(OCCUPIED, true))
                player.startRiding(entity, true)
                sittingStat?.let { player.incrementStat(it) }

                if (player is ServerPlayerEntity) {
                    AdornCriteria.SIT_ON_BLOCK.trigger(player, pos)
                }

                ActionResult.SUCCESS
            }
            ActionResult.SUCCESS
        } else {
            ActionResult.PASS
        }
    }

    override fun onStateReplaced(state: BlockState, world: World, pos: BlockPos, newState: BlockState, moved: Boolean) {
        super.onStateReplaced(state, world, pos, newState, moved)

        if (state.block != newState.block) {
            if (world.isClient || !isSittingEnabled()) return
            world.getEntitiesByType(
                AdornEntities.SEAT,
                Box(getActualSeatPos(world, state, pos)),
                Predicates.alwaysTrue()
            ).forEach {
                it.removeAllPassengers()
                it.kill()
            }
        }
    }

    protected open fun getActualSeatPos(world: World, state: BlockState, pos: BlockPos) = pos

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        if (isSittingEnabled()) builder.add(OCCUPIED)
    }

    protected open fun isSittingEnabled() = true

    companion object {
        @JvmField val OCCUPIED: BooleanProperty = Properties.OCCUPIED
    }
}
