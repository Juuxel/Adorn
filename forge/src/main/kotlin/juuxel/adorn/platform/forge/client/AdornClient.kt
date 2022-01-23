package juuxel.adorn.platform.forge.client

import juuxel.adorn.client.gui.screen.BrewerScreen
import juuxel.adorn.client.gui.screen.DrawerScreen
import juuxel.adorn.client.gui.screen.GuideBookScreen
import juuxel.adorn.client.gui.screen.KitchenCupboardScreen
import juuxel.adorn.client.gui.screen.MainConfigScreen
import juuxel.adorn.client.gui.screen.TradingStationScreen
import juuxel.adorn.menu.AdornMenus
import juuxel.adorn.platform.PlatformBridges
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.ingame.HandledScreens
import net.minecraft.resource.ReloadableResourceManager
import net.minecraft.util.Identifier
import net.minecraftforge.client.ConfigGuiHandler
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import thedarkcolour.kotlinforforge.forge.MOD_BUS

object AdornClient {
    fun init() {
        MOD_BUS.addListener(this::setup)
        MOD_BUS.addListener(AdornRenderers::registerColorProviders)
        MOD_BUS.addListener(AdornRenderers::registerRenderers)
        val resourceManager = MinecraftClient.getInstance().resourceManager as ReloadableResourceManager
        resourceManager.registerReloader(PlatformBridges.resources.bookManager)
        resourceManager.registerReloader(PlatformBridges.resources.colorManager)
        ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory::class.java) {
            ConfigGuiHandler.ConfigGuiFactory { _, parent -> MainConfigScreen(parent) }
        }
        ClientCompatInit.init(MOD_BUS)
    }

    private fun setup(event: FMLClientSetupEvent) {
        AdornRenderers.registerRenderLayers()
        registerScreens()
    }

    private fun registerScreens() {
        HandledScreens.register(AdornMenus.DRAWER, ::DrawerScreen)
        HandledScreens.register(AdornMenus.KITCHEN_CUPBOARD, ::KitchenCupboardScreen)
        HandledScreens.register(AdornMenus.TRADING_STATION, ::TradingStationScreen)
        HandledScreens.register(AdornMenus.BREWER, ::BrewerScreen)
    }

    fun openBookScreen(bookId: Identifier) {
        MinecraftClient.getInstance().setScreen(GuideBookScreen(PlatformBridges.resources.bookManager[bookId]))
    }
}
