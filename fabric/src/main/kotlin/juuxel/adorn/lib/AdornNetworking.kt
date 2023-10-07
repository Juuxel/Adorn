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
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

object AdornNetworking {
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
