package juuxel.adorn.trading

import juuxel.adorn.util.NbtConvertible
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound

data class Trade(var selling: ItemStack, var price: ItemStack) : NbtConvertible {
    private val listeners: MutableList<TradeListener> = ArrayList()

    fun isEmpty() = selling.isEmpty || price.isEmpty

    override fun readNbt(nbt: NbtCompound) {
        selling = ItemStack.fromNbt(nbt.getCompound("Selling"))
        price = ItemStack.fromNbt(nbt.getCompound("Price"))
    }

    override fun writeNbt(nbt: NbtCompound) = nbt.apply {
        put("Selling", selling.writeNbt(NbtCompound()))
        put("Price", price.writeNbt(NbtCompound()))
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
