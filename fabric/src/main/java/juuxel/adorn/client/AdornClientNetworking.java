package juuxel.adorn.client;

import juuxel.adorn.client.book.BookManager;
import juuxel.adorn.client.gui.screen.BrewerScreen;
import juuxel.adorn.client.gui.screen.GuideBookScreen;
import juuxel.adorn.client.resources.BookManagerFabric;
import juuxel.adorn.networking.BrewerFluidSyncS2CMessage;
import juuxel.adorn.networking.OpenBookS2CMessage;
import juuxel.adorn.util.Logging;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import org.slf4j.Logger;

public final class AdornClientNetworking {
    private static final Logger LOGGER = Logging.logger();

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(OpenBookS2CMessage.ID, (payload, context) -> {
            BookManager bookManager = BookManagerFabric.INSTANCE;

            if (bookManager.contains(payload.bookId())) {
                context.client().setScreen(new GuideBookScreen(bookManager.get(payload.bookId())));
            } else {
                LOGGER.error("[Adorn] Undefined guide book: {}", payload.bookId());
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(BrewerFluidSyncS2CMessage.ID, (payload, context) -> {
            BrewerScreen.setFluidFromPacket(context.client(), payload.syncId(), payload.fluid());
        });
    }
}
