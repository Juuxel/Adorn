package juuxel.adorn.menu

import juuxel.adorn.platform.PlatformBridges
import net.minecraft.screen.ScreenHandlerType

object AdornMenus {
    val TRADING_STATION: ScreenHandlerType<TradingStationMenu> by PlatformBridges.menus.register("trading_station", ::TradingStationMenu)

    fun init() {
    }
}
