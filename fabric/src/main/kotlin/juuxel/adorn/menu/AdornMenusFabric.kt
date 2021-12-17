package juuxel.adorn.menu

import juuxel.adorn.AdornCommon
import juuxel.adorn.client.gui.screen.DrawerScreen
import juuxel.adorn.client.gui.screen.KitchenCupboardScreen
import juuxel.adorn.client.gui.screen.TradingStationScreen
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.text.Text

object AdornMenusFabric {
    val DRAWER = register("drawer")
    val KITCHEN_CUPBOARD = register("kitchen_cupboard")

    fun init() {
    }

    @Environment(EnvType.CLIENT)
    fun initClient() {
        registerScreen(DRAWER, ::DrawerScreen)
        registerScreen(KITCHEN_CUPBOARD, ::KitchenCupboardScreen)
        ScreenRegistry.register(AdornMenus.TRADING_STATION, ::TradingStationScreen)
    }

    private fun register(
        id: String
    ): ScreenHandlerType<*> = ScreenHandlerRegistry.registerExtended(AdornCommon.id(id)) { syncId, inventory, buf ->
        val pos = buf.readBlockPos()
        val world = inventory.player.world

        world.getBlockState(pos).createScreenHandlerFactory(world, pos)!!.createMenu(syncId, inventory, inventory.player)
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified M : ScreenHandler> registerScreen(
        type: ScreenHandlerType<*>,
        crossinline screenFn: (M, PlayerEntity, Text) -> HandledScreen<M>
    ) = ScreenRegistry.register(type as ScreenHandlerType<out M>) { menu, inventory, title ->
        screenFn(menu as M, inventory.player, title)
    }
}
