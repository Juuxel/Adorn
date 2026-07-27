package juuxel.adorn.networking;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.client.AdornClient;
import juuxel.adorn.client.gui.screen.BrewerScreen;
import juuxel.adorn.menu.TradingStationMenu;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class AdornNetworking {
    public static void register(RegisterPayloadHandlersEvent event) {
         var registrar = event.registrar(AdornCommon.NAMESPACE);
         registrar.playToClient(OpenBookS2CMessage.ID, OpenBookS2CMessage.PACKET_CODEC, AdornNetworking::handleBookOpen);
         registrar.playToClient(BrewerFluidSyncS2CMessage.ID, BrewerFluidSyncS2CMessage.PACKET_CODEC, AdornNetworking::handleBrewerFluidSync);
         registrar.playToServer(SetTradeStackC2SMessage.ID, SetTradeStackC2SMessage.PACKET_CODEC, AdornNetworking::handleSetTradeStack);
    }

    private static void handleBookOpen(OpenBookS2CMessage message, IPayloadContext context) {
        AdornClient.openBookScreen(message.bookId());
    }

    private static void handleBrewerFluidSync(BrewerFluidSyncS2CMessage message, IPayloadContext context) {
        BrewerScreen.setFluidFromPacket(Minecraft.getInstance(), message.syncId(), message.fluid());
    }

    private static void handleSetTradeStack(SetTradeStackC2SMessage message, IPayloadContext context) {
        var sender = context.player();
        var menu = sender.containerMenu;

        if (menu.containerId == message.syncId() && menu instanceof TradingStationMenu tradingStationMenu) {
            tradingStationMenu.updateTradeStack(message.slotId(), message.stack(), sender);
        }
    }
}
