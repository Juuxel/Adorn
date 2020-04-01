package juuxel.adorn.gui.controller

import juuxel.adorn.Adorn
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.text.Text

class DrawerController(syncId: Int, playerInv: PlayerInventory, context: ScreenHandlerContext, title: Text) :
    SimpleInvController(syncId, playerInv, context, 5, 3, title, PALETTE_ID) {
    companion object {
        private val PALETTE_ID = Adorn.id("drawer")
    }
}
