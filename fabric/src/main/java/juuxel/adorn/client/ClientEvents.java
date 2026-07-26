package juuxel.adorn.client;

import juuxel.adorn.client.gui.TradeTooltipComponent;
import juuxel.adorn.trading.Trade;
import net.fabricmc.fabric.api.client.rendering.v1.ClientTooltipComponentCallback;

public final class ClientEvents {
    public static void init() {
        ClientTooltipComponentCallback.EVENT.register(data -> {
            if (data instanceof Trade trade) {
                return new TradeTooltipComponent(trade);
            }

            return null;
        });
    }
}
