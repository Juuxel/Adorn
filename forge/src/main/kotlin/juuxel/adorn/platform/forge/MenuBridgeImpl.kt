package juuxel.adorn.platform.forge

import juuxel.adorn.platform.MenuBridge
import juuxel.adorn.platform.forge.menu.DrawerMenu
import juuxel.adorn.platform.forge.menu.KitchenCupboardMenu
import juuxel.adorn.platform.forge.menu.TradingStationMenu
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext

object MenuBridgeImpl : MenuBridge {
    override fun drawer(syncId: Int, playerInventory: PlayerInventory, context: ScreenHandlerContext) =
        DrawerMenu(syncId, playerInventory)

    override fun kitchenCupboard(syncId: Int, playerInventory: PlayerInventory, context: ScreenHandlerContext) =
        KitchenCupboardMenu(syncId, playerInventory)

    override fun tradingStation(
        syncId: Int, playerInventory: PlayerInventory, context: ScreenHandlerContext, owner: Boolean
    ): ScreenHandler? {
        if (!owner) return null // this is something we only support on fabric
        return TradingStationMenu(syncId, playerInventory, context)
    }
}
