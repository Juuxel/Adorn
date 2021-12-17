package juuxel.adorn.menu

import juuxel.adorn.platform.PlatformBridges
import net.minecraft.screen.ScreenHandlerType

object AdornMenus {
    val DRAWER: ScreenHandlerType<DrawerMenu> by PlatformBridges.menus.register("drawer", DrawerMenu::load)
    val KITCHEN_CUPBOARD: ScreenHandlerType<KitchenCupboardMenu> by PlatformBridges.menus.register("kitchen_cupboard", KitchenCupboardMenu::load)
    val TRADING_STATION: ScreenHandlerType<TradingStationMenu> by PlatformBridges.menus.register("trading_station", ::TradingStationMenu)

    fun init() {
    }
}
