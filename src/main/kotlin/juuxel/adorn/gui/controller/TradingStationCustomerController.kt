package juuxel.adorn.gui.controller

import io.github.cottonmc.cotton.gui.client.BackgroundPainter
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import juuxel.adorn.gui.controller.TradingStationController.Companion.WHITE
import juuxel.adorn.gui.widget.CenteredLabelWidget
import juuxel.adorn.lib.ModGuis
import juuxel.adorn.util.color
import net.minecraft.container.BlockContext
import net.minecraft.container.SlotActionType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.BasicInventory
import net.minecraft.item.ItemStack
import net.minecraft.network.chat.TranslatableComponent

class TradingStationCustomerController(
    syncId: Int,
    playerInv: PlayerInventory,
    private val blockContext: BlockContext
) : BaseAdornController(
    ModGuis.TRADING_STATION_CUSTOMER,
    syncId,
    playerInv,
    blockContext,
    BasicInventory(0),
    getBlockPropertyDelegate(blockContext)
) {
    init {
        (rootPanel as WGridPanel).apply {
            val tradeInv = TradingStationController.getTrade(blockContext).createInventory()
            val storage = TradingStationController.getStorage(blockContext)

            add(WItemSlot.of(tradeInv, 0), 1, 2)
            add(WItemSlot.of(tradeInv, 1), 1, 4)

            add(CenteredLabelWidget(TranslatableComponent("block.adorn.trading_station.selling"), WHITE), 1, 1)
            add(CenteredLabelWidget(TranslatableComponent("block.adorn.trading_station.price"), WHITE), 1, 3)

            for (row in 0..2) {
                for (col in 0..3) {
                    add(WItemSlot.of(storage, row * 4 + col), 3 + col, 2 + row)
                }
            }

            add(createPlayerInventoryPanel(), 0, 6)
            validate(this@TradingStationCustomerController)
        }
    }

    override fun onSlotClick(slotNumber: Int, button: Int, action: SlotActionType, player: PlayerEntity): ItemStack {
        val slot = slotList.getOrNull(slotNumber)
        val cursorStack = player.inventory.cursorStack

        // Only allow modifying the player inventory
        return if (slot?.inventory is PlayerInventory) {
            super.onSlotClick(slotNumber, button, action, player)
        } else cursorStack
    }

    override fun addPainters() {
        rootPanel.setBackgroundPainter(BackgroundPainter.createColorful(color(0x359668)))
    }

    override fun getTitleColor() = WHITE
}
