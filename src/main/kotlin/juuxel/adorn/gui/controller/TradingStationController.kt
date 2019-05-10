package juuxel.adorn.gui.controller

import io.github.cottonmc.cotton.gui.EmptyInventory
import io.github.cottonmc.cotton.gui.client.BackgroundPainter
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import juuxel.adorn.block.entity.TradingStationBlockEntity
import juuxel.adorn.gui.widget.CenteredLabelWidget
import juuxel.adorn.lib.ModGuis
import juuxel.adorn.util.color
import juuxel.adorn.util.flatten
import net.minecraft.container.BlockContext
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.network.chat.TranslatableComponent
import java.util.Optional

class TradingStationController(syncId: Int, playerInv: PlayerInventory, context: BlockContext) : BaseAdornController(
    ModGuis.TRADING_STATION,
    syncId,
    playerInv,
    context,
    getStorage(context),
    getBlockPropertyDelegate(context)
) {
    init {
        (rootPanel as WGridPanel).apply {
            val be = getBlockEntity(context) as? TradingStationBlockEntity ?: return@apply

            val tradeInventory = be.trade.createInventory()

            add(WItemSlot.of(tradeInventory, 0), 1, 2)
            add(WItemSlot.of(tradeInventory, 1), 1, 4)

            add(CenteredLabelWidget(TranslatableComponent("block.adorn.trading_station.selling"), WHITE), 1, 1)
            add(CenteredLabelWidget(TranslatableComponent("block.adorn.trading_station.price"), WHITE), 1, 3)

            for (row in 0..2) {
                for (col in 0..3) {
                    add(WItemSlot.of(blockInventory, row * 4 + col), 3 + col, 2 + row)
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

        private fun getStorage(context: BlockContext): Inventory = context.run<Optional<Inventory>> { world, pos ->
            Optional.ofNullable((world.getBlockEntity(pos) as? TradingStationBlockEntity)?.storage)
        }.flatten().orElse(EmptyInventory.INSTANCE)
    }
}
