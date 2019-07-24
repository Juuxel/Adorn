package juuxel.adorn.gui.controller

import io.github.cottonmc.cotton.gui.client.BackgroundPainter
import juuxel.adorn.Adorn
import juuxel.adorn.gui.widget.ManagedColorPainter
import juuxel.adorn.resources.ColorManager
import juuxel.adorn.util.color
import net.minecraft.container.BlockContext
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.registry.Registry

class DrawerController(syncId: Int, playerInv: PlayerInventory, private val context: BlockContext) :
    SimpleInvController(
        syncId,
        playerInv,
        context,
        5, 3,
        COLOR_MAP_ID
    ) {
    override fun addPainters() {
        super.addPainters()
//        rootPanel.setBackgroundPainter(BackgroundPainter.createColorful(color(0xD9CEB2)))
        rootPanel.setBackgroundPainter(
            ManagedColorPainter(
                ColorManager.getColors(COLOR_MAP_ID),
                Registry.BLOCK.getId(getBlock(context).get())
            )
        )
    }

    companion object {
        private val COLOR_MAP_ID = Adorn.id("drawer_backgrounds")
    }
}
