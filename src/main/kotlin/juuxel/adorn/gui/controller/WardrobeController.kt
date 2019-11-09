package juuxel.adorn.gui.controller

import juuxel.adorn.Adorn
import net.minecraft.container.BlockContext
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text

class WardrobeController(syncId: Int, playerInv: PlayerInventory, context: BlockContext, title: Text) :
    SimpleInvController(syncId, playerInv, context, 5, 5, title, PALETTE_ID) {
    companion object {
        private val PALETTE_ID = Adorn.id("wardrobe")
    }
}
