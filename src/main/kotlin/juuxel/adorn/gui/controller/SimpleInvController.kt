package juuxel.adorn.gui.controller

import io.github.cottonmc.cotton.gui.client.BackgroundPainter
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import io.github.cottonmc.cotton.gui.widget.WLabel
import juuxel.adorn.gui.widget.Painters
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.container.BlockContext
import net.minecraft.container.PropertyDelegate
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.text.Text

open class SimpleInvController(
    syncId: Int,
    playerInv: PlayerInventory,
    context: BlockContext,
    invWidth: Int,
    invHeight: Int,
    title: Text,
    blockInventory: Inventory = getBlockInventoryOrCreate(context, invWidth * invHeight),
    propertyDelegate: PropertyDelegate = getBlockPropertyDelegate(context)
) : BaseAdornController(syncId, playerInv, context, blockInventory, propertyDelegate) {
    private val slot: WItemSlot

    init {
        (rootPanel as WGridPanel).apply {
            add(WLabel(title, WLabel.DEFAULT_TEXT_COLOR), 0, 0)

            slot = WItemSlot.of(blockInventory, 0, invWidth, invHeight)
            add(slot, (9 - invWidth) / 2, 1)

            if (invHeight > 0) {
                add(playerInvPanel, 0, 2 + invHeight)
            }

            validate(this@SimpleInvController)
        }
    }

    @Environment(EnvType.CLIENT)
    override fun addPainters() {
        super.addPainters()
        rootPanel.setBackgroundPainter(BackgroundPainter.VANILLA)
        slot.setBackgroundPainter(Painters.LIBGUI_STYLE_SLOT)
    }
}
