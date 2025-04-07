package juuxel.adorn.platform.forge.client;

import juuxel.adorn.client.gui.TradeTooltipComponent;
import juuxel.adorn.client.gui.screen.AdornMenuScreens;
import juuxel.adorn.client.gui.screen.GuideBookScreen;
import juuxel.adorn.client.gui.screen.MainConfigScreen;
import juuxel.adorn.platform.PlatformBridges;
import juuxel.adorn.trading.Trade;
import juuxel.adorn.util.Logging;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.util.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.slf4j.Logger;

public final class AdornClient {
    private static final Logger LOGGER = Logging.logger();

    public static void init(IEventBus modBus) {
        modBus.addListener(AdornClient::setup);
        modBus.addListener(AdornRenderers::registerRenderers);
        modBus.addListener(AdornClient::registerTooltipComponent);
        var resourceManager = (ReloadableResourceManagerImpl) MinecraftClient.getInstance().getResourceManager();
        resourceManager.registerReloader(PlatformBridges.get().getResources().getBookManager());
        resourceManager.registerReloader(PlatformBridges.get().getResources().getColorManager());
        ModLoadingContext.get().registerExtensionPoint(
            IConfigScreenFactory.class,
            () -> (container, parent) -> new MainConfigScreen(parent)
        );
    }

    private static void setup(FMLClientSetupEvent event) {
        AdornMenuScreens.register();
    }

    private static void registerTooltipComponent(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(Trade.class, TradeTooltipComponent::new);
    }

    public static void openBookScreen(Identifier bookId) {
        var bookManager = PlatformBridges.get().getResources().getBookManager();

        if (bookManager.contains(bookId)) {
            MinecraftClient.getInstance().setScreen(new GuideBookScreen(bookManager.get(bookId)));
        } else {
            LOGGER.error("[Adorn] Undefined guide book: {}", bookId);
        }
    }
}
