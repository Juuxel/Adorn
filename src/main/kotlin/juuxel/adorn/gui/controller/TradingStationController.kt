package juuxel.adorn.gui.controller

import io.github.cottonmc.cotton.gui.client.BackgroundPainter
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import io.github.cottonmc.cotton.gui.widget.WLabel
import juuxel.adorn.block.entity.TradingStation
import juuxel.adorn.gui.painter.Painters
import juuxel.adorn.gui.widget.CenteredLabelWidget
import juuxel.adorn.gui.widget.DisplayOnlySlot
import juuxel.adorn.lib.ModNetworking
import juuxel.adorn.trading.Trade
import juuxel.adorn.trading.TradeInventory
import juuxel.adorn.util.Colors
import juuxel.adorn.util.color
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.fabricmc.fabric.api.server.PlayerStream
import net.minecraft.container.BlockContext
import net.minecraft.container.SlotActionType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.text.TranslatableText

// TODO: Multiple trades in one station?
class TradingStationController(
    syncId: Int,
    playerInv: PlayerInventory,
    private val blockContext: BlockContext
) : BaseAdornController(
    syncId,
    playerInv,
    blockContext,
    getStorage(blockContext),
    getBlockPropertyDelegate(blockContext)
) {
    // TODO: Remove
    private val slots = ArrayList<WItemSlot>()

    init {
        (rootPanel as WGridPanel).apply {
            add(
                WLabel(
                    TranslatableText(
                        blockContext.run<String> { world, pos -> world.getBlockState(pos).block.translationKey }.get()
                    ), titleColor
                ), 0, 0
            )

            val tradeInv = getTrade(blockContext).createInventory()

            fun WItemSlot.addToSlots() = apply { slots += this }

            add(DisplayOnlySlot(tradeInv, 0).addToSlots(), 1, 2)
            add(DisplayOnlySlot(tradeInv, 1).addToSlots(), 1, 4)

            add(CenteredLabelWidget(TranslatableText("block.adorn.trading_station.selling"), Colors.WHITE), 1, 1)
            add(CenteredLabelWidget(TranslatableText("block.adorn.trading_station.price"), Colors.WHITE), 1, 3)

            add(WItemSlot.of(blockInventory, 0, 4, 3).addToSlots(), 3, 2)

            add(playerInvPanel, 0, 6)
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

    @Environment(EnvType.CLIENT)
    override fun addPainters() {
        super.addPainters()
        rootPanel.setBackgroundPainter(BackgroundPainter.createColorful(color(0x359668)))
        slots.forEach { it.setBackgroundPainter(Painters.LIBGUI_STYLE_SLOT) }
    }

    override fun getTitleColor() = Colors.WHITE

    companion object {

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
