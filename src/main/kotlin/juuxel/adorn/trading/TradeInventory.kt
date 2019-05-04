package juuxel.adorn.trading

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack

// TODO: Make the items behave as ghost items
// TODO: Prevent the customer GUI from destroying items
class TradeInventory(private val trade: Trade, private val forOwner: Boolean) : Inventory {
    override fun getInvStack(slot: Int) = when (slot) {
        0 -> trade.item
        1 -> trade.price
        else -> throw IllegalArgumentException("Invalid slot for TradeInventory: $slot")
    }

    override fun markDirty() {
        trade.callListeners()
    }

    override fun clear() {}

    override fun setInvStack(slot: Int, stack: ItemStack) {
        if (!forOwner) return

        when (slot) {
            0 -> trade.item = stack
            1 -> trade.price = stack
            else -> throw IllegalArgumentException("Invalid slot for TradeInventory: $slot")
        }
    }

    override fun removeInvStack(var1: Int) = ItemStack.EMPTY

    override fun canPlayerUseInv(player: PlayerEntity?) = true

    override fun getInvSize() = 2

    override fun takeInvStack(var1: Int, var2: Int) = ItemStack.EMPTY

    override fun isInvEmpty() = false
}
