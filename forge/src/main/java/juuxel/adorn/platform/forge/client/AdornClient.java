package juuxel.adorn.platform.forge.client;

import juuxel.adorn.client.gui.TradeTooltipComponent;
import juuxel.adorn.client.gui.screen.AdornMenuScreens;
import juuxel.adorn.client.gui.screen.GuideBookScreen;
import juuxel.adorn.client.gui.screen.MainConfigScreen;
import juuxel.adorn.client.gui.screen.ModCompatWarningScreen;
import juuxel.adorn.platform.PlatformBridges;
import juuxel.adorn.platform.neo.client.AdornModels;
import juuxel.adorn.trading.Trade;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.util.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

public final class AdornClient {
    private static boolean hasSeenTitleScreen = false;

    public static void init(IEventBus modBus) {
        modBus.addListener(AdornClient::setup);
        modBus.addListener(AdornRenderers::registerRenderers);
        modBus.addListener(AdornRenderers::registerFeatureRenderers);
        modBus.addListener(AdornClient::registerTooltipComponent);
        var resourceManager = (ReloadableResourceManagerImpl) MinecraftClient.getInstance().getResourceManager();
        resourceManager.registerReloader(PlatformBridges.get().getResources().getBookManager());
        resourceManager.registerReloader(PlatformBridges.get().getResources().getColorManager());
        modBus.register(AdornModels.class);
        ModLoadingContext.get().registerExtensionPoint(
            IConfigScreenFactory.class,
            () -> (container, parent) -> new MainConfigScreen(parent)
        );
        NeoForge.EVENT_BUS.addListener(AdornClient::onScreenOpening);
    }

    private static void setup(FMLClientSetupEvent event) {
        AdornMenuScreens.register();
    }

    private static void registerTooltipComponent(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(Trade.class, TradeTooltipComponent::new);
    }

    public static void openBookScreen(Identifier bookId) {
        MinecraftClient.getInstance().setScreen(new GuideBookScreen(PlatformBridges.get().getResources().getBookManager().get(bookId)));
    }

    private static void onScreenOpening(ScreenEvent.Opening event) {
        if (hasSeenTitleScreen) return;
        if (event.getNewScreen() instanceof TitleScreen) {
            hasSeenTitleScreen = true;
            event.setNewScreen(ModCompatWarningScreen.checkAndCreate(event.getNewScreen(), false));
        }
    }
}
