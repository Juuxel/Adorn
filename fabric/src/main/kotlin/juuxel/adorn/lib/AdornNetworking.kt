package juuxel.adorn.lib

import juuxel.adorn.AdornCommon
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.Entity
import net.minecraft.network.Packet
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket

object AdornNetworking {
    val ENTITY_SPAWN = AdornCommon.id("entity_spawn")

    fun init() {
    }

    @Environment(EnvType.CLIENT)
    fun initClient() {
        ClientPlayNetworking.registerGlobalReceiver(ENTITY_SPAWN) { client, _, buf, _ ->
            val packet = EntitySpawnS2CPacket()
            packet.read(buf)
            client.execute {
                val world = client.player?.world as? ClientWorld ?: return@execute
                val entity = packet.entityTypeId.create(world)!!
                entity.entityId = packet.id
                entity.uuid = packet.uuid
                entity.updatePositionAndAngles(
                    packet.x, packet.y, packet.z,
                    packet.pitch * 360 / 256f, packet.yaw * 360 / 256f
                )
                entity.updateTrackedPosition(packet.x, packet.y, packet.z)
                world.addEntity(packet.id, entity)
            }
        }
    }

    fun createEntitySpawnPacket(entity: Entity): Packet<*> =
        ServerPlayNetworking.createS2CPacket(
            ENTITY_SPAWN,
            PacketByteBufs.create().apply {
                EntitySpawnS2CPacket(entity).write(this)
            }
        )
}
