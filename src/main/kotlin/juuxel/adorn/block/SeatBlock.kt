package juuxel.adorn.block

import com.google.common.base.Predicates
import juuxel.adorn.lib.ModEntities
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.fabricmc.fabric.api.server.PlayerStream
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.client.network.packet.EntityPassengersSetS2CPacket
import net.minecraft.entity.Entity
import net.minecraft.entity.SpawnType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.state.StateFactory
import net.minecraft.state.property.Properties
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BoundingBox
import net.minecraft.world.World

abstract class SeatBlock(settings: Settings) : Block(settings) {
    init {
        defaultState = defaultState.with(OCCUPIED, false)
    }

    open val sittingYOffset: Double = 0.0

    override fun activate(
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hitResult: BlockHitResult
    ): Boolean {
        val actualPos = getActualSeatPos(world, state, pos)
        val actualState = if (pos == actualPos) state else world.getBlockState(actualPos)

        if (state !== actualState && actualState.block !is SeatBlock)
            return false

        val occupied = actualState[OCCUPIED]
        return if (world.isClient) {
            !occupied
        } else if (!world.isClient && !occupied) {
            val entity = ModEntities.SITTING_VEHICLE.spawn(world, null, null, player, actualPos, SpawnType.TRIGGERED, false, false)
            entity?.setPos(actualPos, sittingYOffset)
            world.setBlockState(actualPos, actualState.with(OCCUPIED, true))
            player.startRiding(entity, true)
            true
        } else false
    }

    override fun onBreak(world: World, pos: BlockPos, state: BlockState, player: PlayerEntity) {
        super.onBreak(world, pos, state, player)
        if (world.isClient) return
        world.getEntities(
            ModEntities.SITTING_VEHICLE,
            BoundingBox(getActualSeatPos(world, state, pos)),
            Predicates.alwaysTrue()
        ).forEach {
            it.removeAllPassengers()
            it.kill()
        }
    }

    protected open fun getActualSeatPos(world: World, state: BlockState, pos: BlockPos) = pos

    override fun appendProperties(builder: StateFactory.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(OCCUPIED)
    }

    companion object {
        @JvmField val OCCUPIED = Properties.OCCUPIED
    }
}
