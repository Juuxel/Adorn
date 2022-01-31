package juuxel.adorn.menu

import juuxel.adorn.platform.PlatformBridges
import net.minecraft.menu.MenuType

object AdornMenus {
    val DRAWER: MenuType<DrawerMenu> by PlatformBridges.menus.register("drawer", DrawerMenu::load)
    val KITCHEN_CUPBOARD: MenuType<KitchenCupboardMenu> by PlatformBridges.menus.register("kitchen_cupboard", KitchenCupboardMenu::load)
    val TRADING_STATION: MenuType<TradingStationMenu> by PlatformBridges.menus.register("trading_station", ::TradingStationMenu)
    val BREWER: MenuType<BrewerMenu> by PlatformBridges.menus.register("brewer", ::BrewerMenu)

    fun init() {
    }
}
