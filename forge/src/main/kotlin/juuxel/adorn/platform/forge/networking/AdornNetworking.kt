package juuxel.adorn.platform.forge.networking

import juuxel.adorn.AdornCommon
import juuxel.adorn.platform.forge.client.AdornClient
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.network.NetworkDirection
import net.minecraftforge.network.NetworkEvent
import net.minecraftforge.network.NetworkRegistry
import java.util.Optional
import java.util.concurrent.Callable
import java.util.function.Supplier

object AdornNetworking {
    private const val PROTOCOL = "1"
    val CHANNEL = NetworkRegistry.newSimpleChannel(
        AdornCommon.id("main"),
        { PROTOCOL },
        PROTOCOL::equals,
        PROTOCOL::equals
    )

    fun init() {
        CHANNEL.registerMessage(
            0,
            OpenBookS2CMessage::class.java,
            OpenBookS2CMessage::write,
            OpenBookS2CMessage::fromPacket,
            this::handleBookOpen,
            Optional.of(NetworkDirection.PLAY_TO_CLIENT)
        )
    }

    private fun handleBookOpen(message: OpenBookS2CMessage, context: Supplier<NetworkEvent.Context>) {
        context.get().enqueueWork {
            DistExecutor.unsafeCallWhenOn(Dist.CLIENT) { Callable { AdornClient.openBookScreen(message.bookId) } }
        }
        context.get().packetHandled = true
    }
}
