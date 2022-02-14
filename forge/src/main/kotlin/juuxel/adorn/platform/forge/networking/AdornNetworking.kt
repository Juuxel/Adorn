package juuxel.adorn.platform.forge.networking

import juuxel.adorn.AdornCommon
import juuxel.adorn.client.gui.screen.BrewerScreen
import juuxel.adorn.platform.forge.client.AdornClient
import net.minecraft.client.MinecraftClient
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.network.NetworkDirection
import net.minecraftforge.network.NetworkEvent
import net.minecraftforge.network.NetworkRegistry
import java.util.Optional
import java.util.concurrent.Callable
import java.util.function.Supplier

object AdornNetworking {
    private const val PROTOCOL = "2"
    val CHANNEL = NetworkRegistry.newSimpleChannel(
        AdornCommon.id("main"),
        { PROTOCOL },
        PROTOCOL::equals,
        PROTOCOL::equals
    )

    fun init() {
        var index = 0

        CHANNEL.registerMessage(
            index++,
            OpenBookS2CMessage::class.java,
            OpenBookS2CMessage::write,
            OpenBookS2CMessage::fromPacket,
            this::handleBookOpen,
            Optional.of(NetworkDirection.PLAY_TO_CLIENT)
        )

        CHANNEL.registerMessage(
            index++,
            BrewerFluidSyncS2CMessage::class.java,
            BrewerFluidSyncS2CMessage::write,
            BrewerFluidSyncS2CMessage::fromPacket,
            this::handleBrewerFluidSync,
            Optional.of(NetworkDirection.PLAY_TO_CLIENT)
        )
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
}
