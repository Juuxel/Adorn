package juuxel.adorn.gui.controller

import io.github.cottonmc.cotton.gui.client.BackgroundPainter
import juuxel.adorn.Adorn
import juuxel.adorn.util.color
import net.minecraft.container.BlockContext
import net.minecraft.entity.player.PlayerInventory

// TODO: Background
class KitchenCupboardController(syncId: Int, playerInv: PlayerInventory, context: BlockContext) :
    SimpleInvController(syncId, playerInv, context, 5, 3, Adorn.id("drawer_backgrounds")) {
    override fun addPainters() {
        super.addPainters()
        rootPanel.setBackgroundPainter(BackgroundPainter.createColorful(color(0x99B2B7)))
    }
}
