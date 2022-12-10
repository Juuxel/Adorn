package juuxel.adorn.lib

import juuxel.adorn.AdornCommon
import juuxel.adorn.client.gui.screen.BrewerScreen
import juuxel.adorn.client.gui.screen.GuideBookScreen
import juuxel.adorn.client.resources.BookManagerFabric
import juuxel.adorn.fluid.FluidReference
import juuxel.adorn.fluid.FluidVolume
import juuxel.adorn.menu.TradingStationMenu
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.network.Packet
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

object AdornNetworking {
    val ENTITY_SPAWN = AdornCommon.id("entity_spawn")
    val OPEN_BOOK = AdornCommon.id("open_book")
    val BREWER_FLUID_SYNC = AdornCommon.id("brewer_fluid_sync")
    val SET_TRADE_STACK = AdornCommon.id("set_trade_stack")

    fun init() {
        ServerPlayNetworking.registerGlobalReceiver(SET_TRADE_STACK) { server, player, _, buf, _ ->
            val syncId = buf.readVarInt()
            val slotId = buf.readVarInt()
            val stack = buf.readItemStack()
            server.execute {
                val menu = player.menu
                if (menu.syncId == syncId && menu is TradingStationMenu) {
                    menu.updateTradeStack(slotId, stack, player)
                }
            }
        }
    }

    @Environment(EnvType.CLIENT)
    fun initClient() {
        ClientPlayNetworking.registerGlobalReceiver(ENTITY_SPAWN) { client, _, buf, _ ->
            val packet = EntitySpawnS2CPacket(buf)
            client.execute {
                val world = client.player?.world as? ClientWorld ?: return@execute
                val entity = packet.entityType.create(world)!!
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

        ClientPlayNetworking.registerGlobalReceiver(BREWER_FLUID_SYNC) { client, _, buf, _ ->
            val syncId = buf.readUnsignedByte().toInt()
            val volume = FluidVolume.load(buf)

            client.execute {
                BrewerScreen.setFluidFromPacket(client, syncId, volume)
            }
        }
    }

    fun createEntitySpawnPacket(entity: Entity): Packet<ClientPlayPacketListener> =
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

    fun sendBrewerFluidSync(player: PlayerEntity, syncId: Int, fluid: FluidReference) {
        if (player is ServerPlayerEntity) {
            val buf = PacketByteBufs.create()
            buf.writeByte(syncId)
            fluid.write(buf)
            ServerPlayNetworking.send(player, BREWER_FLUID_SYNC, buf)
        }
    }

    @Environment(EnvType.CLIENT)
    fun sendSetTradeStack(syncId: Int, slotId: Int, stack: ItemStack) {
        val buf = PacketByteBufs.create()
        buf.writeVarInt(syncId)
        buf.writeVarInt(slotId)
        buf.writeItemStack(stack)
        ClientPlayNetworking.send(SET_TRADE_STACK, buf)
    }
}
