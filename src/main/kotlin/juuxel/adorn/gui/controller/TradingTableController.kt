package juuxel.adorn.gui.controller

import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WLabel
import juuxel.adorn.block.entity.TradingTableBlockEntity
import net.minecraft.container.BlockContext
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.StringTextComponent

class TradingTableController(syncId: Int, playerInv: PlayerInventory, context: BlockContext) :
    BaseAdornController(syncId, playerInv, context) {
    init {
        (rootPanel as WGridPanel).apply {
            // TODO: L10n
            val be = getBlockEntity(context) as? TradingTableBlockEntity ?: return@apply
            if (be.ownerName != null) {
                add(WLabel(StringTextComponent("Owner: ").append(be.ownerName), 0x404040), 0, 1)
            }

            val playerInvY = if (be.owner == playerInv.player.gameProfile.id) {
                setupOwnerGui(this)
            } else {
                setupCustomerGui(this)
            }

            add(createPlayerInventoryPanel(), 0, playerInvY)
            validate(this@TradingTableController)
        }
    }

    /**
     * @return the player inventory Y position
     */
    private fun setupCustomerGui(rootPanel: WGridPanel): Int {
        rootPanel.add(WLabel("WIP: Customer GUI"), 0, 2)
        return 3
    }

    /**
     * @return the player inventory Y position
     */
    private fun setupOwnerGui(rootPanel: WGridPanel): Int {
        rootPanel.add(WLabel("WIP: Owner GUI"), 0, 2)
        return 3
    }
}
