package juuxel.adorn.platform.forge.networking

import juuxel.adorn.AdornCommon
import juuxel.adorn.client.gui.screen.BrewerScreen
import juuxel.adorn.menu.TradingStationMenu
import juuxel.adorn.platform.forge.client.AdornClient
import net.minecraft.client.MinecraftClient
import net.minecraft.network.PacketByteBuf
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.network.NetworkDirection
import net.minecraftforge.network.NetworkEvent
import net.minecraftforge.network.NetworkRegistry
import java.util.Optional
import java.util.concurrent.Callable
import java.util.function.Supplier

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
        noinline listener: (M, Supplier<NetworkEvent.Context>) -> Unit,
        direction: NetworkDirection
    ) {
        CHANNEL.registerMessage(nextId++, M::class.java, Message::write, decoder, listener, Optional.of(direction))
    }

    fun init() {
        register(OpenBookS2CMessage::fromPacket, this::handleBookOpen, NetworkDirection.PLAY_TO_CLIENT)
        register(BrewerFluidSyncS2CMessage::fromPacket, this::handleBrewerFluidSync, NetworkDirection.PLAY_TO_CLIENT)
        register(SetTradeStackC2SMessage::fromPacket, this::handleSetTradeStack, NetworkDirection.PLAY_TO_SERVER)
    }

    private fun handleBookOpen(message: OpenBookS2CMessage, context: Supplier<NetworkEvent.Context>) {
        context.get().enqueueWork {
            DistExecutor.unsafeCallWhenOn(Dist.CLIENT) { Callable { AdornClient.openBookScreen(message.bookId) } }
        }
        context.get().packetHandled = true
    }

    private fun handleBrewerFluidSync(message: BrewerFluidSyncS2CMessage, context: Supplier<NetworkEvent.Context>) {
        context.get().enqueueWork {
            DistExecutor.unsafeCallWhenOn(Dist.CLIENT) {
                Callable {
                    BrewerScreen.setFluidFromPacket(MinecraftClient.getInstance(), message.syncId, message.fluid)
                }
            }
        }
        context.get().packetHandled = true
    }

    private fun handleSetTradeStack(message: SetTradeStackC2SMessage, context: Supplier<NetworkEvent.Context>) {
        context.get().enqueueWork {
            val sender = context.get().sender!!
            val menu = sender.menu

            if (menu.syncId == message.syncId && menu is TradingStationMenu) {
                menu.updateTradeStack(message.slotId, message.stack, sender)
            }
        }
        context.get().packetHandled = true
    }
}
