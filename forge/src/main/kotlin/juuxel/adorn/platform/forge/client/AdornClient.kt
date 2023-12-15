package juuxel.adorn.platform.forge.client

import juuxel.adorn.client.gui.TradeTooltipComponent
import juuxel.adorn.client.gui.screen.AdornMenuScreens
import juuxel.adorn.client.gui.screen.GuideBookScreen
import juuxel.adorn.client.gui.screen.MainConfigScreen
import juuxel.adorn.platform.PlatformBridges
import juuxel.adorn.platform.forge.Adorn.Companion.MOD_BUS
import juuxel.adorn.trading.Trade
import net.minecraft.client.MinecraftClient
import net.minecraft.resource.ReloadableResourceManagerImpl
import net.minecraft.util.Identifier
import net.neoforged.fml.ModLoadingContext
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.ConfigScreenHandler
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent

object AdornClient {
    fun init() {
        MOD_BUS.addListener(this::setup)
        MOD_BUS.addListener(AdornRenderers::registerRenderers)
        MOD_BUS.addListener(this::registerTooltipComponent)
        val resourceManager = MinecraftClient.getInstance().resourceManager as ReloadableResourceManagerImpl
        resourceManager.registerReloader(PlatformBridges.resources.bookManager)
        resourceManager.registerReloader(PlatformBridges.resources.colorManager)
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory::class.java) {
            ConfigScreenHandler.ConfigScreenFactory { _, parent -> MainConfigScreen(parent) }
        }
    }

    private fun setup(event: FMLClientSetupEvent) {
        AdornMenuScreens.register()
    }

    private fun registerTooltipComponent(event: RegisterClientTooltipComponentFactoriesEvent) {
        event.register(Trade::class.java, ::TradeTooltipComponent)
    }

    fun openBookScreen(bookId: Identifier) {
        MinecraftClient.getInstance().setScreen(GuideBookScreen(PlatformBridges.resources.bookManager[bookId]))
    }
}
