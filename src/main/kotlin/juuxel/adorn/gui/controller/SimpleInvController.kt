package juuxel.adorn.gui.controller

import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import io.github.cottonmc.cotton.gui.widget.WLabel
import net.minecraft.container.BlockContext
import net.minecraft.container.PropertyDelegate
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.text.TranslatableText

open class SimpleInvController(
    syncId: Int,
    playerInv: PlayerInventory,
    context: BlockContext,
    invWidth: Int,
    invHeight: Int,
    blockInventory: Inventory = getBlockInventoryOrCreate(context, invWidth * invHeight),
    propertyDelegate: PropertyDelegate = getBlockPropertyDelegate(context)
) : BaseAdornController(syncId, playerInv, context, blockInventory, propertyDelegate) {
    init {
        (rootPanel as WGridPanel).apply {
            add(
                WLabel(
                    TranslatableText(
                        context.run<String> { world, pos -> world.getBlockState(pos).block.translationKey }.get()
                    ), titleColor
                ), 0, 0
            )

            for (row in 0 until invHeight) {
                for (col in 0 until invWidth) {
                    val hasEvenWidth = invWidth % 2 == 0
                    // Creates a gap for even-width inventories, looks really weird
                    val xOffset =
                        if (hasEvenWidth && col + 1 > invWidth / 2) 1
                        else 0
                    val slot = WItemSlot.of(blockInventory, col + row * invWidth)
                    add(slot, col + (9 - invWidth) / 2 + xOffset, row + 1)
                }
            }

            if (invHeight > 0) {
                add(createPlayerInventoryPanel(), 0, 2 + invHeight)
            }

            validate(this@SimpleInvController)
        }
    }
}
