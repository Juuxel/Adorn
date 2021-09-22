package juuxel.adorn.menu

import juuxel.adorn.client.gui.screen.DrawerScreen
import juuxel.adorn.client.gui.screen.KitchenCupboardScreen
import juuxel.adorn.client.gui.screen.TradingStationScreen
import juuxel.adorn.platform.PlatformBridges
import juuxel.adorn.platform.Registrar
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.gui.screen.ingame.HandledScreens
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.text.Text
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

object AdornMenus {
    val MENUS: Registrar<ScreenHandlerType<*>> = PlatformBridges.registrarFactory.menu()

    val DRAWER by MENUS.register("drawer") { ScreenHandlerType(::DrawerMenu) }
    val KITCHEN_CUPBOARD by MENUS.register("kitchen_cupboard") { ScreenHandlerType(::KitchenCupboardMenu) }
    val TRADING_STATION by MENUS.register("trading_station") { ScreenHandlerType(::TradingStationMenu) }

    @OnlyIn(Dist.CLIENT)
    fun initClient() {
        registerScreen(DRAWER, ::DrawerScreen)
        registerScreen(KITCHEN_CUPBOARD, ::KitchenCupboardScreen)
        registerScreen(TRADING_STATION, ::TradingStationScreen)
    }

    private inline fun <M : ScreenHandler> registerScreen(
        type: ScreenHandlerType<M>,
        crossinline screenFn: (M, PlayerInventory, Text) -> HandledScreen<M>
    ) {
        HandledScreens.register(type) { menu, inventory, title ->
            screenFn(menu, inventory, title)
        }
    }
}
