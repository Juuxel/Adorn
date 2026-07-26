package juuxel.adorn.lib;

import juuxel.adorn.menu.TradingStationMenu;
import juuxel.adorn.networking.BrewerFluidSyncS2CMessage;
import juuxel.adorn.networking.OpenBookS2CMessage;
import juuxel.adorn.networking.SetTradeStackC2SMessage;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public final class AdornNetworking {
    public static void init() {
        PayloadTypeRegistry.playC2S().register(SetTradeStackC2SMessage.ID, SetTradeStackC2SMessage.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(BrewerFluidSyncS2CMessage.ID, BrewerFluidSyncS2CMessage.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(OpenBookS2CMessage.ID, OpenBookS2CMessage.PACKET_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(SetTradeStackC2SMessage.ID, (payload, context) -> {
            var menu = context.player().containerMenu;
            if (menu.containerId == payload.syncId() && menu instanceof TradingStationMenu tradingStationMenu) {
                tradingStationMenu.updateTradeStack(payload.slotId(), payload.stack(), context.player());
            }
        });
    }
}
