package juuxel.adorn.gui.controller

import io.github.cottonmc.cotton.gui.client.BackgroundPainter
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import juuxel.adorn.block.entity.TradingStation
import juuxel.adorn.gui.widget.CenteredLabelWidget
import juuxel.adorn.lib.ModGuis
import juuxel.adorn.lib.ModNetworking
import juuxel.adorn.trading.Trade
import juuxel.adorn.trading.TradeInventory
import juuxel.adorn.util.color
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.fabricmc.fabric.api.server.PlayerStream
import net.minecraft.container.BlockContext
import net.minecraft.container.SlotActionType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.network.chat.TranslatableComponent

// TODO: Multiple trades in one station?
class TradingStationController(
    syncId: Int,
    playerInv: PlayerInventory,
    private val blockContext: BlockContext
) : BaseAdornController(
    ModGuis.TRADING_STATION,
    syncId,
    playerInv,
    blockContext,
    getStorage(blockContext),
    getBlockPropertyDelegate(blockContext)
) {
    init {
        (rootPanel as WGridPanel).apply {
            val tradeInv = getTrade(blockContext).createInventory()

            add(WItemSlot.of(tradeInv, 0), 1, 2)
            add(WItemSlot.of(tradeInv, 1), 1, 4)

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

    override fun onSlotClick(slotNumber: Int, button: Int, action: SlotActionType, player: PlayerEntity): ItemStack {
        val slot = slotList.getOrNull(slotNumber)

        return if (slot?.inventory is TradeInventory) {
            val cursorStack = player.inventory.cursorStack

            when (action) {
                SlotActionType.PICKUP -> {
                    slot.stack = cursorStack.copy()
                    slot.markDirty()

                    if (!world.isClient) {
                        blockContext.run { world, pos ->
                            PlayerStream.watching(world, pos).forEach {
                                ServerSidePacketRegistry.INSTANCE.sendToPlayer(
                                    it,
                                    ModNetworking.createTradeSyncPacket(pos, getTrade(blockContext))
                                )
                            }
                        }
                    }

                    cursorStack
                }

                else -> cursorStack
            }
        } else {
            super.onSlotClick(slotNumber, button, action, player)
        }
    }

    override fun addPainters() {
        rootPanel.setBackgroundPainter(BackgroundPainter.createColorful(color(0x359668)))
    }

    override fun getTitleColor() = WHITE

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
        fun getStorage(context: BlockContext): Inventory = getOrCreateTradingStation(context).storage

        /**
         * Gets the [TradingStation.trade] of the trading station at the [context]'s location.
         * Uses [getOrCreateTradingStation] for finding a trading station.
         */
        fun getTrade(context: BlockContext): Trade = getOrCreateTradingStation(context).trade
    }
}
