package juuxel.adorn.gui.controller

import io.github.cottonmc.cotton.gui.client.BackgroundPainter
import juuxel.adorn.Adorn
import juuxel.adorn.util.color
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.container.BlockContext
import net.minecraft.entity.player.PlayerInventory

class KitchenCupboardController(syncId: Int, playerInv: PlayerInventory, context: BlockContext) :
    SimpleInvController(syncId, playerInv, context, 5, 3, PALETTE_ID) {
    @Environment(EnvType.CLIENT)
    override fun addPainters() {
        super.addPainters()
        rootPanel.setBackgroundPainter(BackgroundPainter.createColorful(color(0x99B2B7)))
    }

    companion object {
        private val PALETTE_ID = Adorn.id("kitchen_cupboard_colors")
    }
}
