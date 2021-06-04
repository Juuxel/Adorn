package juuxel.adorn.platform.fabric

import juuxel.adorn.menu.DrawerMenu
import juuxel.adorn.menu.KitchenCupboardMenu
import juuxel.adorn.menu.TradingStationMenu
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext

object MenuBridgeImpl {
    @JvmStatic
    fun createDrawerMenu(syncId: Int, playerInventory: PlayerInventory, context: ScreenHandlerContext): ScreenHandler {
        return DrawerMenu(syncId, playerInventory, context)
    }

    @JvmStatic
    fun createKitchenCupboardMenu(
        syncId: Int, playerInventory: PlayerInventory, context: ScreenHandlerContext
    ): ScreenHandler {
        return KitchenCupboardMenu(syncId, playerInventory, context)
    }

    @JvmStatic
    fun createTradingStationMenu(
        syncId: Int, playerInventory: PlayerInventory, context: ScreenHandlerContext, owner: Boolean
    ): ScreenHandler {
        return TradingStationMenu(syncId, playerInventory, context, owner)
    }
}
