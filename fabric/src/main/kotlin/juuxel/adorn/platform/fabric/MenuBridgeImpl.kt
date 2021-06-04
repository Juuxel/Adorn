package juuxel.adorn.platform.fabric

import juuxel.adorn.menu.DrawerMenu
import juuxel.adorn.menu.KitchenCupboardMenu
import juuxel.adorn.menu.TradingStationMenu
import juuxel.adorn.platform.MenuBridge
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext

object MenuBridgeImpl : MenuBridge {
    override fun createDrawerMenu(
        syncId: Int, playerInventory: PlayerInventory, context: ScreenHandlerContext
    ): ScreenHandler = DrawerMenu(syncId, playerInventory, context)

    override fun createKitchenCupboardMenu(
        syncId: Int, playerInventory: PlayerInventory, context: ScreenHandlerContext
    ): ScreenHandler = KitchenCupboardMenu(syncId, playerInventory, context)

    override fun createTradingStationMenu(
        syncId: Int, playerInventory: PlayerInventory, context: ScreenHandlerContext, owner: Boolean
    ): ScreenHandler = TradingStationMenu(syncId, playerInventory, context, owner)
}
