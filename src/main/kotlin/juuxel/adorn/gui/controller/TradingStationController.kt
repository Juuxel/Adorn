package juuxel.adorn.gui.controller

import io.github.cottonmc.cotton.gui.client.BackgroundPainter
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import juuxel.adorn.block.entity.TradingStation
import juuxel.adorn.gui.widget.CenteredLabelWidget
import juuxel.adorn.lib.ModGuis
import juuxel.adorn.trading.Trade
import juuxel.adorn.util.CompoundInventory
import juuxel.adorn.util.color
import net.minecraft.container.BlockContext
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.network.chat.TranslatableComponent

class TradingStationController(syncId: Int, playerInv: PlayerInventory, context: BlockContext) : BaseAdornController(
    ModGuis.TRADING_STATION,
    syncId,
    playerInv,
    context,
    getCombinedStationInv(context),
    getBlockPropertyDelegate(context)
) {
    init {
        (rootPanel as WGridPanel).apply {
            add(WItemSlot.of(blockInventory, 0), 1, 2)
            add(WItemSlot.of(blockInventory, 1), 1, 4)

            add(CenteredLabelWidget(TranslatableComponent("block.adorn.trading_station.selling"), WHITE), 1, 1)
            add(CenteredLabelWidget(TranslatableComponent("block.adorn.trading_station.price"), WHITE), 1, 3)

            for (row in 0..2) {
                for (col in 0..3) {
                    add(WItemSlot.of(blockInventory, row * 4 + col + 2), 3 + col, 2 + row)
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

        /**
         * Gets the [juuxel.adorn.block.entity.TradingStationBlockEntity] at the [context]'s location.
         * If it's not present, creates an empty trading station using [TradingStation.createEmpty].
         */
        private fun getOrCreateTradingStation(context: BlockContext) =
            getBlockEntity(context) as? TradingStation ?: TradingStation.createEmpty()

        /**
         * Gets the [TradingStation.storage] of the trading station at the [context]'s location.
         * Uses [getOrCreateTradingStation] for finding a trading station.
         */
        private fun getStorage(context: BlockContext): Inventory = getOrCreateTradingStation(context).storage

        /**
         * Gets the [TradingStation.trade] of the trading station at the [context]'s location.
         * Uses [getOrCreateTradingStation] for finding a trading station.
         */
        private fun getTrade(context: BlockContext): Trade = getOrCreateTradingStation(context).trade

        /**
         * Gets a combined view of the trade and storage of the trading station at the [context]'s location.
         * Uses [getTrade] and [getStorage].
         */
        private fun getCombinedStationInv(context: BlockContext) = CompoundInventory(
            getTrade(context).createInventory(),
            getStorage(context)
        )
    }
}
