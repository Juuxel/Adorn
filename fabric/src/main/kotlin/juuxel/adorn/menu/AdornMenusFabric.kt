package juuxel.adorn.menu

import juuxel.adorn.client.gui.screen.BrewerScreen
import juuxel.adorn.client.gui.screen.DrawerScreen
import juuxel.adorn.client.gui.screen.KitchenCupboardScreen
import juuxel.adorn.client.gui.screen.TradingStationScreen
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry

object AdornMenusFabric {
    @Environment(EnvType.CLIENT)
    fun initClient() {
        ScreenRegistry.register(AdornMenus.DRAWER, ::DrawerScreen)
        ScreenRegistry.register(AdornMenus.KITCHEN_CUPBOARD, ::KitchenCupboardScreen)
        ScreenRegistry.register(AdornMenus.TRADING_STATION, ::TradingStationScreen)
        ScreenRegistry.register(AdornMenus.BREWER, ::BrewerScreen)
    }
}
