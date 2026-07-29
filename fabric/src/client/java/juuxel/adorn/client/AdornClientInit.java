package juuxel.adorn.client;

import juuxel.adorn.client.book.BookManager;
import juuxel.adorn.client.gui.screen.AdornMenuScreens;
import juuxel.adorn.client.renderer.AdornRenderers;
import juuxel.adorn.client.resources.AdornClientResources;
import net.fabricmc.api.ClientModInitializer;

public final class AdornClientInit implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AdornRenderers.init();
        AdornMenuScreens.register();
        AdornClientNetworking.init();
        AdornClientResources.init();
        ClientEvents.init();
        AdornModels.init();
        BookManager.setupTooltipProvider();
    }
}
