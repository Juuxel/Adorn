package juuxel.adorn.lib

import juuxel.adorn.AdornCommon
import juuxel.adorn.client.gui.screen.GuideBookScreen
import juuxel.adorn.client.resources.BookManagerFabric
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.Packet
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

object AdornNetworking {
    val ENTITY_SPAWN = AdornCommon.id("entity_spawn")
    val OPEN_BOOK = AdornCommon.id("open_book")

    fun init() {
    }

    @Environment(EnvType.CLIENT)
    fun initClient() {
        ClientPlayNetworking.registerGlobalReceiver(ENTITY_SPAWN) { client, _, buf, _ ->
            val packet = EntitySpawnS2CPacket(buf)
            client.execute {
                val world = client.player?.world as? ClientWorld ?: return@execute
                val entity = packet.entityTypeId.create(world)!!
                entity.id = packet.id
                entity.uuid = packet.uuid
                entity.updatePositionAndAngles(
                    packet.x, packet.y, packet.z,
                    packet.pitch * 360 / 256f, packet.yaw * 360 / 256f
                )
                entity.updateTrackedPosition(packet.x, packet.y, packet.z)
                world.addEntity(packet.id, entity)
            }
        }

        ClientPlayNetworking.registerGlobalReceiver(OPEN_BOOK) { client, _, buf, _ ->
            val bookId = buf.readIdentifier()
            client.execute {
                client.setScreen(GuideBookScreen(BookManagerFabric[bookId]))
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

    fun sendOpenBookPacket(player: PlayerEntity, bookId: Identifier) {
        if (player is ServerPlayerEntity) {
            val buf = PacketByteBufs.create()
            buf.writeIdentifier(bookId)
            ServerPlayNetworking.send(player, OPEN_BOOK, buf)
        }
    }
}
