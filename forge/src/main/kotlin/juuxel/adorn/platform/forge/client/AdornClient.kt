package juuxel.adorn.platform.forge.client

import juuxel.adorn.client.gui.screen.TradingStationScreen
import juuxel.adorn.menu.AdornMenus
import juuxel.adorn.platform.PlatformBridges
import juuxel.adorn.platform.forge.menu.AdornMenusForge
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.ingame.HandledScreens
import net.minecraft.resource.ReloadableResourceManager
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import thedarkcolour.kotlinforforge.forge.MOD_BUS

object AdornClient {
    fun init() {
        MOD_BUS.addListener(this::setup)
        MOD_BUS.addListener(AdornRenderers::registerColorProviders)
        MOD_BUS.addListener(AdornRenderers::registerRenderers)
        (MinecraftClient.getInstance().resourceManager as ReloadableResourceManager).registerReloader(PlatformBridges.resources.colorManager)
    }

    private fun setup(event: FMLClientSetupEvent) {
        AdornRenderers.registerRenderLayers()
        AdornMenusForge.initClient()
        registerScreens()
    }

    private fun registerScreens() {
        HandledScreens.register(AdornMenus.TRADING_STATION, ::TradingStationScreen)
    }
}
