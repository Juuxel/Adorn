package juuxel.adorn.menu

import juuxel.adorn.block.AdornBlocks
import juuxel.adorn.block.entity.TradingStation
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.screen.slot.Slot
import net.minecraft.screen.slot.SlotActionType
import net.minecraftforge.common.util.Constants
import org.apache.logging.log4j.LogManager
import java.util.function.BiFunction

class TradingStationMenu(
    syncId: Int,
    playerInventory: Inventory,
    private val context: ScreenHandlerContext = ScreenHandlerContext.EMPTY
) : ScreenHandler(AdornMenus.TRADING_STATION.get(), syncId) {
    private val tradingStation: TradingStation
    private val sellingSlot: Slot
    private val priceSlot: Slot

    init {
        val slot = 18

        tradingStation = getTradingStation(context)
        val tradeInventory = tradingStation.trade.createInventory()
        val storage = tradingStation.storage

        sellingSlot = addSlot(TradeSlot(tradeInventory, 0, 26, 36))
        priceSlot = addSlot(TradeSlot(tradeInventory, 1, 26, 72))

        // Storage
        for (y in 0..2) {
            for (x in 0..3) {
                addSlot(Slot(storage, x + y * 4, 62 + x * slot, 36 + y * slot))
            }
        }

        // Main player inventory
        for (y in 0..2) {
            for (x in 0..8) {
                addSlot(Slot(playerInventory, y * 9 + x, 8 + x * slot, 104 + y * slot))
            }
        }

        // Hotbar
        for (x in 0..8) {
            addSlot(Slot(playerInventory, 3 * 9 + x, 8 + x * slot, 162))
        }
    }

    override fun canUse(player: PlayerEntity) =
        canUse(context, player, AdornBlocks.TRADING_STATION.get())

    override fun transferSlot(player: PlayerEntity, index: Int): ItemStack {
        val offset = 2

        // Ghost slots
        if (index in 0 until offset) return ItemStack.EMPTY

        var result = ItemStack.EMPTY
        val slot = slots[index]

        if (slot != null && slot.hasStack()) {
            val containerSize = 12
            val stack = slot.stack
            result = stack.copy()

            if (offset <= index && index < containerSize + offset) {
                if (!insertItem(stack, containerSize + offset, slots.size, true)) {
                    return ItemStack.EMPTY
                }
            } else if (!insertItem(stack, offset, containerSize + offset, false)) {
                return ItemStack.EMPTY
            }

            if (stack.isEmpty) {
                slot.stack = ItemStack.EMPTY
            } else {
                slot.markDirty()
            }
        }

        return result
    }

    override fun onSlotClick(slotNumber: Int, button: Int, action: SlotActionType, player: PlayerEntity): ItemStack {
        val slot = slots.getOrNull(slotNumber)
        val cursorStack = player.inventory.cursorStack

        if (action == SlotActionType.PICKUP && slot is TradeSlot) {
            slot.stack = cursorStack.copy()
            slot.markDirty()

            if (tradingStation is BlockEntity) {
                val state = tradingStation.cachedState
                player.world.updateListeners(tradingStation.pos, state, state, Constants.BlockFlags.BLOCK_UPDATE)
            }

            return cursorStack
        }

        return super.onSlotClick(slotNumber, button, action, player)
    }

    private class TradeSlot(inventory: Inventory, index: Int, x: Int, y: Int) : Slot(inventory, index, x, y) {
        override fun canTakeItems(player: PlayerEntity) = false
        override fun canInsert(stack: ItemStack) = false
        override fun takeStack(count: Int): ItemStack = ItemStack.EMPTY
    }

    companion object {
        private val LOGGER = LogManager.getLogger()

        /**
         * Gets the [juuxel.adorn.block.entity.TradingStationBlockEntity] at the [context]'s location.
         * If it's not present, creates an empty trading station using [TradingStation.createEmpty].
         */
        private fun getTradingStation(context: ScreenHandlerContext): TradingStation =
            context.run(BiFunction { world, pos -> world.getBlockEntity(pos) as TradingStation })
                .orElseGet {
                    LOGGER.warn("[Adorn] Trading station not found, creating fake one")
                    TradingStation.createEmpty()
                }
    }
}
