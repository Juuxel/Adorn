package juuxel.adorn.client.lib

import juuxel.adorn.client.gui.screen.BrewerScreen
import juuxel.adorn.client.gui.screen.GuideBookScreen
import juuxel.adorn.resources.BookManagerFabric
import juuxel.adorn.fluid.FluidVolume
import juuxel.adorn.lib.AdornNetworking
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.world.ClientWorld
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket

object ClientNetworking {
    fun init() {
        ClientPlayNetworking.registerGlobalReceiver(AdornNetworking.ENTITY_SPAWN) { client, _, buf, _ ->
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

        ClientPlayNetworking.registerGlobalReceiver(AdornNetworking.OPEN_BOOK) { client, _, buf, _ ->
            val bookId = buf.readIdentifier()
            client.execute {
                client.setScreen(GuideBookScreen(BookManagerFabric[bookId]))
            }
        }

        ClientPlayNetworking.registerGlobalReceiver(AdornNetworking.BREWER_FLUID_SYNC) { client, _, buf, _ ->
            val syncId = buf.readUnsignedByte().toInt()
            val volume = FluidVolume.load(buf)

            client.execute {
                BrewerScreen.setFluidFromPacket(client, syncId, volume)
            }
        }
    }
}
