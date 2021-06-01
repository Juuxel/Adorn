package juuxel.adorn.menu

import juuxel.adorn.AdornCommon
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext

class KitchenCupboardMenu(syncId: Int, playerInv: PlayerInventory, context: ScreenHandlerContext) :
    SimpleMenu(AdornMenus.KITCHEN_CUPBOARD, syncId, playerInv, context, 5, 3, PALETTE_ID) {
    constructor(syncId: Int, playerInv: PlayerInventory) : this(syncId, playerInv, ScreenHandlerContext.EMPTY)

    companion object {
        private val PALETTE_ID = AdornCommon.id("kitchen_cupboard")
    }
}
