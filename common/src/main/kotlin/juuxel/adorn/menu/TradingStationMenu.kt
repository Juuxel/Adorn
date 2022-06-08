package juuxel.adorn.menu

import juuxel.adorn.block.AdornBlocks
import juuxel.adorn.block.entity.TradingStation
import juuxel.adorn.item.TradingStationUpgradeItem
import juuxel.adorn.util.getBlockEntity
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.menu.Menu
import net.minecraft.menu.MenuContext
import net.minecraft.menu.Slot
import net.minecraft.menu.SlotActionType

class TradingStationMenu(
    syncId: Int,
    playerInventory: Inventory,
    private val context: MenuContext = MenuContext.EMPTY
) : Menu(AdornMenus.TRADING_STATION, syncId) {
    private val tradingStation: TradingStation
    private val sellingSlot: Slot
    private val priceSlot: Slot
    /*
    private val storageUpgradeStorageA: InventoryComponent
    private val storageUpgradeStorageB: InventoryComponent
    private val storageUpgradeSlotsA: SlotGroup
    private val storageUpgradeSlotsB: SlotGroup
    */

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
                addSlot(StorageSlot(storage, x + y * 4, 62 + x * slot, 36 + y * slot))
            }
        }

        // Main player inventory
        for (y in 0..2) {
            for (x in 0..8) {
                addSlot(Slot(playerInventory, x + y * 9 + 9, 8 + x * slot, 104 + y * slot))
            }
        }

        // Hotbar
        for (x in 0..8) {
            addSlot(Slot(playerInventory, x, 8 + x * slot, 162))
        }

        // Upgrades
        for (y in 0 until UPGRADE_COUNT) {
            addSlot(UpgradeSlot(tradingStation.upgradeStorage, y, 152, 54 + y * slot))
        }

        /*
        // Storages within storage upgrades
        storageUpgradeStorageA = ItemStackInventory.ofInventoryStack(tradingStation.upgradeStorage, slot = 0, size = 9)
        storageUpgradeStorageB = ItemStackInventory.ofInventoryStack(tradingStation.upgradeStorage, slot = 1, size = 9)
        storageUpgradeSlotsA = SlotGroup(storageUpgradeStorageA, width = 3, height = 3, slotFactory = ::StorageSlot)
        storageUpgradeSlotsB = SlotGroup(storageUpgradeStorageB, width = 3, height = 3, slotFactory = ::StorageSlot)
        setupForStorageUpgrades(0, storageUpgradeSlotsA)
        setupForStorageUpgrades(1, storageUpgradeSlotsB)
        */
    }

    private fun setupForStorageUpgrades(index: Int, slotGroup: SlotGroup) {
        slotGroup.addTo(this)
        tradingStation.upgradeStorage.addListener {
            val item = it.getStack(index).item
            val visible = item is TradingStationUpgradeItem && item.type == TradingStationUpgradeItem.Type.STORAGE

            if (visible) {
                slotGroup.show()
                slotGroup.move(186, 7 + getUpgradePanelY(index))
            } else {
                slotGroup.hide()
            }
        }
    }

    fun getUpgradePanelY(slot: Int): Int {
        if (slot == 0) {
            return 0
        }

        var y = 0

        for (i in 0 until slot) {
            val item = tradingStation.upgradeStorage.getStack(i).item
            if (item is TradingStationUpgradeItem) {
                y += item.type.panelHeight
            }
        }

        return y
    }

    override fun canUse(player: PlayerEntity) =
        canUse(context, player, AdornBlocks.TRADING_STATION)

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

    override fun onSlotClick(slotNumber: Int, button: Int, action: SlotActionType, player: PlayerEntity) {
        val slot = slots.getOrNull(slotNumber)

        if (action == SlotActionType.PICKUP && slot is TradeSlot) {
            if (isValidItem(cursorStack)) {
                slot.stack = cursorStack.copy()
                slot.markDirty()

                if (tradingStation is BlockEntity) {
                    val state = tradingStation.cachedState
                    player.world.updateListeners(tradingStation.pos, state, state, Block.NOTIFY_LISTENERS)
                }
            }

            return
        } else {
            super.onSlotClick(slotNumber, button, action, player)
        }
    }

    private class TradeSlot(inventory: Inventory, index: Int, x: Int, y: Int) : Slot(inventory, index, x, y) {
        override fun canTakeItems(player: PlayerEntity) = false
        override fun canInsert(stack: ItemStack) = false
        override fun takeStack(count: Int): ItemStack = ItemStack.EMPTY
    }

    private class StorageSlot(inventory: Inventory, index: Int, x: Int, y: Int) : ToggleableSlot(inventory, index, x, y) {
        override fun canInsert(stack: ItemStack): Boolean = isValidItem(stack)
    }

    private class UpgradeSlot(inventory: Inventory, index: Int, x: Int, y: Int) : Slot(inventory, index, x, y) {
        override fun canInsert(stack: ItemStack): Boolean {
            val item = stack.item

            if (item is TradingStationUpgradeItem) {
                val type = item.type

                for (slot in 0 until inventory.size()) {
                    if (slot == index) continue

                    val otherItem = inventory.getStack(slot).item
                    if (otherItem is TradingStationUpgradeItem && !TradingStationUpgradeItem.Type.canCombine(type, otherItem.type)) {
                        return false
                    }
                }

                return true
            }

            return false
        }

        override fun getMaxItemCount(): Int = 1
    }

    companion object {
        const val UPGRADE_COUNT = 2

        /**
         * Gets the [juuxel.adorn.block.entity.TradingStationBlockEntity] at the [context]'s location.
         * If it's not present, creates an empty trading station using [TradingStation.createEmpty].
         */
        private fun getTradingStation(context: MenuContext): TradingStation =
            context.getBlockEntity() as? TradingStation ?: run {
                TradingStation.createEmpty()
            }

        private fun isValidItem(stack: ItemStack): Boolean =
            stack.item.canBeNested()
    }
}
