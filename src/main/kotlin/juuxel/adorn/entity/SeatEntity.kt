package juuxel.adorn.entity

import juuxel.adorn.block.SeatBlock
import juuxel.adorn.util.getBlockPos
import juuxel.adorn.util.putBlockPos
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.Packet
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.network.NetworkHooks
import net.minecraftforge.fml.network.PacketDistributor

class SeatEntity(type: EntityType<*>, world: World) : Entity(type, world) {
    init {
        noClip = true
        isInvulnerable = true
    }

    private var seatPos = BlockPos(this.pos)

    fun setPos(pos: BlockPos, blockOffset: Double) {
        check(!world.isClient) {
            "setPos must be called on the logical server"
        }
        updatePosition(pos.x + 0.5, pos.y + 0.25 + blockOffset, pos.z + 0.5)
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
            PacketDistributor.TRACKING_ENTITY.with { this }.send(EntityPassengersSetS2CPacket(this))
        }
        super.kill()
        val state = world.getBlockState(seatPos)
        if (state.block is SeatBlock) {
            world.setBlockState(seatPos, state.with(SeatBlock.OCCUPIED, false))
        }
    }

    override fun canClimb() = false
    override fun collides() = false
    override fun getMountedHeightOffset() = 0.0
    override fun hasNoGravity() = true
    override fun isInvisible() = true
    override fun initDataTracker() {}

    override fun readCustomDataFromTag(tag: CompoundTag) {
        seatPos = tag.getBlockPos("SeatPos")
    }

    override fun writeCustomDataToTag(tag: CompoundTag) {
        tag.putBlockPos("SeatPos", seatPos)
    }

    override fun createSpawnPacket(): Packet<*> = NetworkHooks.getEntitySpawningPacket(this)
}
