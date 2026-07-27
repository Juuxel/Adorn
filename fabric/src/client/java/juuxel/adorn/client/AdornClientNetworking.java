package juuxel.adorn.client;

import juuxel.adorn.client.gui.screen.BrewerScreen;
import juuxel.adorn.client.gui.screen.GuideBookScreen;
import juuxel.adorn.client.resources.BookManagerFabric;
import juuxel.adorn.networking.BrewerFluidSyncS2CMessage;
import juuxel.adorn.networking.OpenBookS2CMessage;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public final class AdornClientNetworking {
    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(OpenBookS2CMessage.ID, (payload, context) -> {
            context.client().setScreen(new GuideBookScreen(BookManagerFabric.INSTANCE.get(payload.bookId())));
        });

        ClientPlayNetworking.registerGlobalReceiver(BrewerFluidSyncS2CMessage.ID, (payload, context) -> {
            BrewerScreen.setFluidFromPacket(context.client(), payload.syncId(), payload.fluid());
        });
    }
}
