package juuxel.adorn.menu

import juuxel.adorn.Adorn
import juuxel.adorn.client.gui.screen.DrawerScreen
import juuxel.adorn.client.gui.screen.KitchenCupboardScreen
import juuxel.adorn.client.gui.screen.TradingStationScreen
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry
import net.minecraft.client.gui.screen.ingame.MenuScreen
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.menu.Menu
import net.minecraft.menu.MenuType
import net.minecraft.text.Text

object AdornMenus {
    val DRAWER = register("drawer")
    val KITCHEN_CUPBOARD = register("kitchen_cupboard")
    val TRADING_STATION = register("trading_station")

    fun init() {
    }

    @Environment(EnvType.CLIENT)
    fun initClient() {
        registerScreen(DRAWER, ::DrawerScreen)
        registerScreen(KITCHEN_CUPBOARD, ::KitchenCupboardScreen)
        registerScreen(TRADING_STATION, ::TradingStationScreen)
    }

    private fun register(
        id: String
    ): MenuType<*> = ScreenHandlerRegistry.registerExtended(Adorn.id(id)) { syncId, inventory, buf ->
        val pos = buf.readBlockPos()
        val world = inventory.player.world

        world.getBlockState(pos).createMenuFactory(world, pos)!!.createMenu(syncId, inventory, inventory.player)
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified M : Menu> registerScreen(
        type: MenuType<*>,
        crossinline screenFn: (M, PlayerEntity, Text) -> MenuScreen<M>
    ) = ScreenRegistry.register(type as MenuType<out M>) { menu, inventory, title ->
        screenFn(menu as M, inventory.player, title)
    }
}
