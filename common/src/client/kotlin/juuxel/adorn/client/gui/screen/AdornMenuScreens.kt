package juuxel.adorn.client.gui.screen

import juuxel.adorn.menu.AdornMenus
import net.minecraft.client.gui.screen.ingame.MenuScreens

object AdornMenuScreens {
    fun register() {
        MenuScreens.register(AdornMenus.DRAWER, ::DrawerScreen)
        MenuScreens.register(AdornMenus.KITCHEN_CUPBOARD, ::KitchenCupboardScreen)
        MenuScreens.register(AdornMenus.TRADING_STATION, ::TradingStationScreen)
        MenuScreens.register(AdornMenus.BREWER, ::BrewerScreen)
    }
}
