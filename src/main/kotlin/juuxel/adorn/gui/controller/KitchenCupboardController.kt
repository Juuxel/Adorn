package juuxel.adorn.gui.controller

import juuxel.adorn.Adorn
import net.minecraft.container.BlockContext
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text

class KitchenCupboardController(syncId: Int, playerInv: PlayerInventory, context: BlockContext, title: Text) :
    SimpleInvController(syncId, playerInv, context, 5, 3, title, PALETTE_ID) {
    companion object {
        private val PALETTE_ID = Adorn.id("kitchen_cupboard")
    }
}
