package juuxel.adorn.trading

import juuxel.adorn.util.NbtConvertible
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag

data class Trade(var selling: ItemStack, var price: ItemStack) : NbtConvertible {
    private val listeners: MutableList<TradeListener> = ArrayList()

    fun isEmpty() = selling.isEmpty || price.isEmpty

    override fun fromTag(tag: CompoundTag) {
        selling = ItemStack.fromTag(tag.getCompound("Selling"))
        price = ItemStack.fromTag(tag.getCompound("Price"))
    }

    override fun toTag(tag: CompoundTag) = tag.apply {
        put("Selling", selling.toTag(CompoundTag()))
        put("Price", price.toTag(CompoundTag()))
    }

    fun addListener(listener: TradeListener) {
        listeners += listener
    }

    fun callListeners() {
        for (listener in listeners) {
            listener.onTradeChanged(this)
        }
    }

    /**
     * Creates a modifiable inventory for this trade.
     */
    fun createInventory() = TradeInventory(this)

    fun interface TradeListener {
        fun onTradeChanged(trade: Trade)
    }
}
