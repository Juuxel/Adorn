package juuxel.adorn.platform.forge.client;

import juuxel.adorn.platform.forge.menu.AdornMenus;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class AdornClient {
    public static void init() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        // TODO: MOD_BUS.addListener(AdornBlocks::registerColorProviders)
        modBus.addListener(AdornClient::setup);
    }

    private static void setup(FMLClientSetupEvent event) {
        // TODO: AdornBlocks.initClient()
        // TODO: AdornEntities.initClient()
        AdornMenus.initClient();
    }
}
