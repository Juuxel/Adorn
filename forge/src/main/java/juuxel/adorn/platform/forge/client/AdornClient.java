package juuxel.adorn.platform.forge.client;

import juuxel.adorn.platform.forge.menu.AdornMenus;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class AdornClient {
    public static void init() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        modBus.addListener(AdornClient::setup);
        modBus.addListener(AdornRenderers.INSTANCE::registerColorProviders);
        modBus.addListener(AdornRenderers.INSTANCE::registerRenderers);
    }

    private static void setup(FMLClientSetupEvent event) {
        AdornRenderers.INSTANCE.registerRenderLayers();
        AdornMenus.initClient();
    }
}
