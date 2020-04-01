package juuxel.adorn.lib

import io.netty.buffer.Unpooled
import juuxel.adorn.Adorn
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.Entity
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket

object AdornNetworking {
    val ENTITY_SPAWN = Adorn.id("entity_spawn")

    fun init() {
    }

    @Environment(EnvType.CLIENT)
    fun initClient() {
        ClientSidePacketRegistry.INSTANCE.register(ENTITY_SPAWN) { context, buf ->
            val packet = EntitySpawnS2CPacket()
            packet.read(buf)
            context.taskQueue.execute {
                val world = context.player?.world ?: return@execute
                val entity = packet.entityTypeId.create(world)!!
                entity.entityId = packet.id
                entity.uuid = packet.uuid
                entity.updateTrackedPosition(packet.x, packet.y, packet.z)
                entity.pitch = packet.pitch * 360 / 256f
                entity.yaw = packet.yaw * 360 / 256f

                (context.player.world as? ClientWorld)?.addEntity(packet.id, entity)
            }
        }
    }

    fun createEntitySpawnPacket(entity: Entity) =
        CustomPayloadS2CPacket(ENTITY_SPAWN, PacketByteBuf(Unpooled.buffer()).apply {
            EntitySpawnS2CPacket(entity).write(this)
        })
}
