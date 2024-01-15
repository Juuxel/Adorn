package juuxel.adorn.platform.forge.networking

import juuxel.adorn.AdornCommon
import juuxel.adorn.client.gui.screen.BrewerScreen
import juuxel.adorn.menu.TradingStationMenu
import juuxel.adorn.platform.forge.client.AdornClient
import net.minecraft.client.MinecraftClient
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.DistExecutor
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent
import net.neoforged.neoforge.network.handling.PlayPayloadContext
import java.util.concurrent.Callable
import java.util.function.Consumer

object AdornNetworking {
    val OPEN_BOOK = AdornCommon.id("open_book")
    val BREWER_FLUID_SYNC = AdornCommon.id("brewer_fluid_sync")
    val SET_TRADE_STACK = AdornCommon.id("set_trade_stack")

    @SubscribeEvent
    fun register(event: RegisterPayloadHandlerEvent) {
        val registrar = event.registrar(AdornCommon.NAMESPACE)
        registrar.play(OPEN_BOOK, OpenBookS2CMessage::fromPacket, Consumer { it.client(this::handleBookOpen) })
        registrar.play(BREWER_FLUID_SYNC, BrewerFluidSyncS2CMessage::fromPacket, Consumer { it.client(this::handleBrewerFluidSync) })
        registrar.play(SET_TRADE_STACK, SetTradeStackC2SMessage::fromPacket, Consumer { it.server(this::handleSetTradeStack) })
    }

    private fun handleBookOpen(message: OpenBookS2CMessage, context: PlayPayloadContext) {
        context.workHandler.execute {
            DistExecutor.unsafeCallWhenOn(Dist.CLIENT) { Callable { AdornClient.openBookScreen(message.bookId) } }
        }
    }

    private fun handleBrewerFluidSync(message: BrewerFluidSyncS2CMessage, context: PlayPayloadContext) {
        context.workHandler.execute {
            DistExecutor.unsafeCallWhenOn(Dist.CLIENT) {
                Callable {
                    BrewerScreen.setFluidFromPacket(MinecraftClient.getInstance(), message.syncId, message.fluid)
                }
            }
        }
    }

    private fun handleSetTradeStack(message: SetTradeStackC2SMessage, context: PlayPayloadContext) {
        context.workHandler.execute {
            val sender = context.player.orElseThrow()
            val menu = sender.menu

            if (menu.syncId == message.syncId && menu is TradingStationMenu) {
                menu.updateTradeStack(message.slotId, message.stack, sender)
            }
        }
    }
}
