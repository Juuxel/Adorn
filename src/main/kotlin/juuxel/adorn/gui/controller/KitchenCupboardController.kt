package juuxel.adorn.gui.controller

import juuxel.adorn.Adorn
import juuxel.adorn.gui.AdornGuis
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext

class KitchenCupboardController(syncId: Int, playerInv: PlayerInventory, context: ScreenHandlerContext) :
    SimpleInvController(AdornGuis.KITCHEN_CUPBOARD, syncId, playerInv, context, 5, 3, PALETTE_ID) {
    constructor(syncId: Int, playerInv: PlayerInventory) : this(syncId, playerInv, ScreenHandlerContext.EMPTY)

    companion object {
        private val PALETTE_ID = Adorn.id("kitchen_cupboard")
    }
}
