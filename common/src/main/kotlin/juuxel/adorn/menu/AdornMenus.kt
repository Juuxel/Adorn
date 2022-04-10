package juuxel.adorn.menu

import juuxel.adorn.platform.PlatformBridges
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.menu.Menu
import net.minecraft.menu.MenuType
import net.minecraft.network.PacketByteBuf

object AdornMenus {
    val MENUS = PlatformBridges.registrarFactory.menu()
    val DRAWER: MenuType<DrawerMenu> by MENUS.register("drawer") { createType(DrawerMenu::load) }
    val KITCHEN_CUPBOARD: MenuType<KitchenCupboardMenu> by MENUS.register("kitchen_cupboard") { createType(KitchenCupboardMenu::load) }
    val TRADING_STATION: MenuType<TradingStationMenu> by MENUS.register("trading_station") { MenuType(::TradingStationMenu) }
    val BREWER: MenuType<BrewerMenu> by MENUS.register("brewer") { MenuType(::BrewerMenu) }

    fun init() {
    }

    private fun <M : Menu> createType(factory: (Int, PlayerInventory, PacketByteBuf) -> M) =
        PlatformBridges.menus.createType(factory)
}
