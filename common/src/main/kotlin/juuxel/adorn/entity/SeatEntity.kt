package juuxel.adorn.entity

import juuxel.adorn.block.SeatBlock
import juuxel.adorn.platform.PlatformBridges
import juuxel.adorn.util.getBlockPos
import juuxel.adorn.util.putBlockPos
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.joml.Vector3f

class SeatEntity(type: EntityType<*>, world: World) : Entity(type, world) {
    init {
        noClip = true
        isInvulnerable = true
    }

    private var seatPos = BlockPos.ofFloored(this.pos)
        set(value) {
            field = value
            dataTracker.set(SEAT_POS, value)
        }

    fun setPos(pos: BlockPos) {
        check(!world.isClient) {
            "setPos must be called on the logical server"
        }
        updatePosition(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5)
        seatPos = pos
    }

    override fun interact(player: PlayerEntity, hand: Hand): ActionResult {
        player.startRiding(this)
        return ActionResult.SUCCESS
    }

    override fun removePassenger(entity: Entity?) {
        super.removePassenger(entity)
        kill()
    }

    override fun kill() {
        removeAllPassengers()
        if (!world.isClient) {
            PlatformBridges.network.sendToTracking(this, EntityPassengersSetS2CPacket(this))
        }
        super.kill()
        val state = world.getBlockState(seatPos)
        if (state.block is SeatBlock) {
            world.setBlockState(seatPos, state.with(SeatBlock.OCCUPIED, false))
        }
    }

    override fun hasNoGravity() = true
    override fun isInvisible() = true

    override fun initDataTracker() {
        dataTracker.startTracking(SEAT_POS, BlockPos.ORIGIN)
    }

    override fun readCustomDataFromNbt(tag: NbtCompound) {
        seatPos = tag.getBlockPos("SeatPos")
    }

    override fun writeCustomDataToNbt(tag: NbtCompound) {
        tag.putBlockPos("SeatPos", seatPos)
    }

    override fun getPassengerAttachmentPos(passenger: Entity, dimensions: EntityDimensions, scaleFactor: Float): Vector3f {
        val seatPos = dataTracker.get(SEAT_POS)
        val state = world.getBlockState(seatPos)
        val block = state.block

        // Add the offset that comes from the block's shape
        val blockOffset = if (block is SeatBlock) {
            block.getSittingOffset(world, state, seatPos)
        } else {
            0.0
        }
        // Remove the inherent offset that comes from this entity not being directly where the block is
        val posOffset = y - seatPos.y

        return Vector3f(0f, (blockOffset - posOffset).toFloat(), 0f)
    }

    companion object {
        private val SEAT_POS: TrackedData<BlockPos> = DataTracker.registerData(SeatEntity::class.java, TrackedDataHandlerRegistry.BLOCK_POS)
    }
}
