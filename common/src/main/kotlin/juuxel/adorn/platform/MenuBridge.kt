package juuxel.adorn.platform

import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext

interface MenuBridge {
    fun createDrawerMenu(syncId: Int, playerInventory: PlayerInventory, context: ScreenHandlerContext): ScreenHandler
    fun createKitchenCupboardMenu(syncId: Int, playerInventory: PlayerInventory, context: ScreenHandlerContext): ScreenHandler
    fun createTradingStationMenu(syncId: Int, playerInventory: PlayerInventory, context: ScreenHandlerContext, owner: Boolean): ScreenHandler
}
