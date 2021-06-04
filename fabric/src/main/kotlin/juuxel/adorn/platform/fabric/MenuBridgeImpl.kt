package juuxel.adorn.platform.fabric

import juuxel.adorn.menu.DrawerMenu
import juuxel.adorn.menu.KitchenCupboardMenu
import juuxel.adorn.menu.TradingStationMenu
import juuxel.adorn.platform.MenuBridge
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext

object MenuBridgeImpl : MenuBridge {
    override fun drawer(syncId: Int, playerInventory: PlayerInventory, context: ScreenHandlerContext): ScreenHandler =
        DrawerMenu(syncId, playerInventory, context)

    override fun kitchenCupboard(syncId: Int, playerInventory: PlayerInventory, context: ScreenHandlerContext) =
        KitchenCupboardMenu(syncId, playerInventory, context)

    override fun tradingStation(
        syncId: Int, playerInventory: PlayerInventory, context: ScreenHandlerContext, owner: Boolean
    ): ScreenHandler = TradingStationMenu(syncId, playerInventory, context, owner)
}
