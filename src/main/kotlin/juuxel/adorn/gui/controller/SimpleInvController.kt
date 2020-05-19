package juuxel.adorn.gui.controller

import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import juuxel.adorn.client.gui.painter.Painters
import juuxel.adorn.gui.widget.WColorableLabel
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.screen.PropertyDelegate
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

open class SimpleInvController(
    syncId: Int,
    playerInv: PlayerInventory,
    context: ScreenHandlerContext,
    invWidth: Int,
    invHeight: Int,
    title: Text,
    private val paletteId: Identifier,
    blockInventory: Inventory = getBlockInventoryOrCreate(context, invWidth * invHeight),
    propertyDelegate: PropertyDelegate = getBlockPropertyDelegate(context)
) : BaseAdornController(syncId, playerInv, context, blockInventory, propertyDelegate) {
    private val slot: WItemSlot
    private val blockId: Identifier = Registry.BLOCK.getId(getBlock(context))

    init {
        (rootPanel as WGridPanel).apply {
            add(WColorableLabel(title, paletteId, blockId), 0, 0)

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
        rootPanel.backgroundPainter = Painters.palette(paletteId, blockId)
        slot.setBackgroundPainter(Painters.LIBGUI_STYLE_SLOT)
    }
}
