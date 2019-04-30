package juuxel.adorn.gui.controller

import io.github.cottonmc.cotton.gui.client.BackgroundPainter
import juuxel.adorn.util.color
import net.minecraft.container.BlockContext
import net.minecraft.entity.player.PlayerInventory

class KitchenCupboardController(syncId: Int, playerInv: PlayerInventory, context: BlockContext) :
    BaseAdornController(syncId, playerInv, context, 5, 3) {
    override fun addPainters() {
        rootPanel.setBackgroundPainter(BackgroundPainter.createColorful(color(0x99B2B7)))
    }
}
