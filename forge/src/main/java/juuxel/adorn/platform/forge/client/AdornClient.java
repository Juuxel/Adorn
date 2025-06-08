package juuxel.adorn.platform.forge.client;

import juuxel.adorn.client.book.BookManager;
import juuxel.adorn.client.gui.TradeTooltipComponent;
import juuxel.adorn.client.gui.screen.AdornMenuScreens;
import juuxel.adorn.client.gui.screen.GuideBookScreen;
import juuxel.adorn.client.gui.screen.MainConfigScreen;
import juuxel.adorn.client.resources.ColorManager;
import juuxel.adorn.platform.PlatformBridges;
import juuxel.adorn.platform.ResourceBridge;
import juuxel.adorn.platform.neo.client.AdornModels;
import juuxel.adorn.trading.Trade;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

public final class AdornClient {
    public static void init(IEventBus modBus) {
        modBus.addListener(AdornClient::setup);
        modBus.addListener(AdornRenderers::registerRenderers);
        modBus.addListener(AdornClient::registerTooltipComponent);
        modBus.addListener(AdornClient::registerReloaders);
        modBus.register(AdornModels.class);
        ModLoadingContext.get().registerExtensionPoint(
            IConfigScreenFactory.class,
            () -> (container, parent) -> new MainConfigScreen(parent)
        );
    }

    private static void registerReloaders(AddClientReloadListenersEvent event) {
        ResourceBridge resources = PlatformBridges.get().getResources();
        event.addListener(BookManager.ID, resources.getBookManager());
        event.addListener(ColorManager.ID, resources.getColorManager());
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
}
