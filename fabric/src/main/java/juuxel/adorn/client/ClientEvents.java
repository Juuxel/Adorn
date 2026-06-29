package juuxel.adorn.client;

import juuxel.adorn.client.gui.TradeTooltipComponent;
import juuxel.adorn.client.gui.screen.ModCompatWarningScreen;
import juuxel.adorn.trading.Trade;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.gui.screen.TitleScreen;

public final class ClientEvents {
    private static boolean hasSeenTitleScreen = false;

    public static void init() {
        TooltipComponentCallback.EVENT.register(data -> {
            if (data instanceof Trade trade) {
                return new TradeTooltipComponent(trade);
            }

            return null;
        });

        ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof TitleScreen) {
                if (hasSeenTitleScreen) return;
                hasSeenTitleScreen = true;

                ScreenEvents.beforeTick(screen).register(titleScreen -> {
                    client.setScreen(ModCompatWarningScreen.checkAndCreate(titleScreen, false));
                });
            }
        });
    }
}
