package juuxel.adorn.gui.controller

import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import io.github.cottonmc.cotton.gui.widget.WLabel
import juuxel.adorn.block.entity.TradingTableBlockEntity
import juuxel.adorn.gui.widget.ArrowWidget
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

            val forOwner = be.owner == playerInv.player.gameProfile.id
            val tradeInventory = be.trade.createInventory(forOwner)

            add(WItemSlot.of(tradeInventory, 0), 1, 2)
            add(WItemSlot.of(tradeInventory, 1), 4, 2)
            add(ArrowWidget(), 2, 2, 2, 1)

            val playerInvY = if (forOwner) {
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
        rootPanel.add(WLabel("WIP: Customer GUI"), 0, 3)
        return 4
    }

    /**
     * @return the player inventory Y position
     */
    private fun setupOwnerGui(rootPanel: WGridPanel): Int {
        rootPanel.add(WLabel("WIP: Owner GUI"), 0, 3)
        return 4
    }
}
