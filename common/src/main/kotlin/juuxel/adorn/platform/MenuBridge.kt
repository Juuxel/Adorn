package juuxel.adorn.platform

import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext

interface MenuBridge {
    fun drawer(syncId: Int, playerInventory: PlayerInventory, context: ScreenHandlerContext): ScreenHandler
    fun kitchenCupboard(syncId: Int, playerInventory: PlayerInventory, context: ScreenHandlerContext): ScreenHandler
    fun tradingStation(syncId: Int, playerInventory: PlayerInventory, context: ScreenHandlerContext, owner: Boolean): ScreenHandler?
}
