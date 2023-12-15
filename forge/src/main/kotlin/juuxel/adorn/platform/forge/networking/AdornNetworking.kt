package juuxel.adorn.platform.forge.networking

import juuxel.adorn.AdornCommon
import juuxel.adorn.client.gui.screen.BrewerScreen
import juuxel.adorn.menu.TradingStationMenu
import juuxel.adorn.platform.forge.client.AdornClient
import net.minecraft.client.MinecraftClient
import net.minecraft.network.PacketByteBuf
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.DistExecutor
import net.neoforged.neoforge.network.INetworkDirection
import net.neoforged.neoforge.network.NetworkEvent
import net.neoforged.neoforge.network.NetworkRegistry
import net.neoforged.neoforge.network.PlayNetworkDirection
import java.util.Optional
import java.util.concurrent.Callable

object AdornNetworking {
    private const val PROTOCOL = "3"
    val CHANNEL = NetworkRegistry.newSimpleChannel(
        AdornCommon.id("main"),
        { PROTOCOL },
        PROTOCOL::equals,
        PROTOCOL::equals
    )

    private var nextId = 0

    @Suppress("INACCESSIBLE_TYPE")
    private inline fun <reified M : Message> register(
        noinline decoder: (PacketByteBuf) -> M,
        noinline listener: (M, NetworkEvent.Context) -> Unit,
        direction: INetworkDirection<*>
    ) {
        CHANNEL.registerMessage(nextId++, M::class.java, Message::write, decoder, listener, Optional.of(direction))
    }

    fun init() {
        register(OpenBookS2CMessage::fromPacket, this::handleBookOpen, PlayNetworkDirection.PLAY_TO_CLIENT)
        register(BrewerFluidSyncS2CMessage::fromPacket, this::handleBrewerFluidSync, PlayNetworkDirection.PLAY_TO_CLIENT)
        register(SetTradeStackC2SMessage::fromPacket, this::handleSetTradeStack, PlayNetworkDirection.PLAY_TO_SERVER)
    }

    private fun handleBookOpen(message: OpenBookS2CMessage, context: NetworkEvent.Context) {
        context.enqueueWork {
            DistExecutor.unsafeCallWhenOn(Dist.CLIENT) { Callable { AdornClient.openBookScreen(message.bookId) } }
        }
        context.packetHandled = true
    }

    private fun handleBrewerFluidSync(message: BrewerFluidSyncS2CMessage, context: NetworkEvent.Context) {
        context.enqueueWork {
            DistExecutor.unsafeCallWhenOn(Dist.CLIENT) {
                Callable {
                    BrewerScreen.setFluidFromPacket(MinecraftClient.getInstance(), message.syncId, message.fluid)
                }
            }
        }
        context.packetHandled = true
    }

    private fun handleSetTradeStack(message: SetTradeStackC2SMessage, context: NetworkEvent.Context) {
        context.enqueueWork {
            val sender = context.sender!!
            val menu = sender.menu

            if (menu.syncId == message.syncId && menu is TradingStationMenu) {
                menu.updateTradeStack(message.slotId, message.stack, sender)
            }
        }
        context.packetHandled = true
    }
}
