package juuxel.adorn.gui.controller

import juuxel.adorn.Adorn
import juuxel.adorn.gui.painter.Painters
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.container.BlockContext
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.registry.Registry

class KitchenCupboardController(syncId: Int, playerInv: PlayerInventory, private val context: BlockContext) :
    SimpleInvController(syncId, playerInv, context, 5, 3, PALETTE_ID) {
    @Environment(EnvType.CLIENT)
    override fun addPainters() {
        super.addPainters()
        rootPanel.setBackgroundPainter(
            Painters.palette(PALETTE_ID, Registry.BLOCK.getId(getBlock(context).get()))
        )
    }

    companion object {
        private val PALETTE_ID = Adorn.id("kitchen_cupboard")
    }
}
