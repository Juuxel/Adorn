package juuxel.adorn.gui.controller

import io.github.cottonmc.cotton.gui.client.BackgroundPainter
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import juuxel.adorn.block.entity.TradingStationBlockEntity
import juuxel.adorn.gui.widget.CenteredLabelWidget
import juuxel.adorn.util.color
import net.minecraft.container.BlockContext
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.TranslatableTextComponent

class TradingStationController(syncId: Int, playerInv: PlayerInventory, context: BlockContext) :
    BaseAdornController(syncId, playerInv, context, WHITE) {
    init {
        (rootPanel as WGridPanel).apply {
            val be = getBlockEntity(context) as? TradingStationBlockEntity ?: return@apply

            val tradeInventory = be.trade.createInventory()

            add(WItemSlot.of(tradeInventory, 0), 1, 2)
            add(WItemSlot.of(tradeInventory, 1), 1, 4)

            add(CenteredLabelWidget(TranslatableTextComponent("block.adorn.trading_station.selling"), WHITE), 1, 1)
            add(CenteredLabelWidget(TranslatableTextComponent("block.adorn.trading_station.price"), WHITE), 1, 3)

            for (row in 0..2) {
                for (col in 0..3) {
                    add(WItemSlot.of(be.storage, row * 4 + col), 3 + col, 2 + row)
                }
            }

            add(createPlayerInventoryPanel(), 0, 6)
            validate(this@TradingStationController)
        }
    }

    override fun addPainters() {
        rootPanel.setBackgroundPainter(BackgroundPainter.createColorful(color(0x359668)))
    }

    companion object {
        val WHITE = color(0xFFFFFF)
    }
}
