package juuxel.adorn.entity

import com.mojang.datafixers.Dynamic
import juuxel.adorn.block.SeatBlock
import juuxel.adorn.lib.AdornNetworking
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.fabricmc.fabric.api.server.PlayerStream
import net.minecraft.client.network.packet.EntityPassengersSetS2CPacket
import net.minecraft.client.network.packet.EntityPositionS2CPacket
import net.minecraft.datafixers.NbtOps
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class SittingVehicleEntity(type: EntityType<*>, world: World) : Entity(type, world) {
    init {
        noClip = true
        isInvulnerable = true
    }

    private var seatPos = BlockPos(this)

    fun setPos(pos: BlockPos, blockOffset: Double) {
        check(!world.isClient) {
            "setPos must be called on the logical server"
        }
        x = pos.x + 0.5
        y = pos.y + 0.25 + blockOffset
        z = pos.z + 0.5
        seatPos = pos
        PlayerStream.watching(this).forEach {
            ServerSidePacketRegistry.INSTANCE.sendToPlayer(it, EntityPositionS2CPacket(this))
        }
    }

    override fun interact(player: PlayerEntity, hand: Hand): Boolean {
        player.startRiding(this)
        return true
    }

    override fun removePassenger(entity: Entity?) {
        super.removePassenger(entity)
        kill()
    }

    override fun kill() {
        removeAllPassengers()
        if (!world.isClient) {
            PlayerStream.watching(this).forEach {
                ServerSidePacketRegistry.INSTANCE.sendToPlayer(it, EntityPassengersSetS2CPacket(this))
                // TODO: Fix removing passengers when breaking the block
            }
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
    override fun createSpawnPacket() = AdornNetworking.createEntitySpawnPacket(this)
    override fun hasNoGravity() = true
    override fun isInvisible() = true

    override fun initDataTracker() {}
    override fun readCustomDataFromTag(tag: CompoundTag) {
        seatPos = BlockPos.deserialize(Dynamic(NbtOps.INSTANCE, tag.getTag("SeatPos")))
    }

    override fun writeCustomDataToTag(tag: CompoundTag) {
        tag.put("SeatPos", seatPos.serialize(NbtOps.INSTANCE))
    }
}
