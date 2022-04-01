package juuxel.adorn.menu

import juuxel.adorn.platform.PlatformBridges
import net.minecraft.menu.MenuType

object AdornMenus {
    val MENUS = PlatformBridges.registrarFactory.menu()
    val DRAWER: MenuType<DrawerMenu> by MENUS.register("drawer") { PlatformBridges.menus.create(DrawerMenu::load) }
    val KITCHEN_CUPBOARD: MenuType<KitchenCupboardMenu> by MENUS.register("kitchen_cupboard") { PlatformBridges.menus.create(KitchenCupboardMenu::load) }
    val TRADING_STATION: MenuType<TradingStationMenu> by MENUS.register("trading_station") { PlatformBridges.menus.create(::TradingStationMenu) }
    val BREWER: MenuType<BrewerMenu> by MENUS.register("brewer") { PlatformBridges.menus.create(::BrewerMenu) }

    fun init() {
    }
}
