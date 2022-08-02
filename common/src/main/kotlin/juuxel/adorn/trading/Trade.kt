package juuxel.adorn.trading

import juuxel.adorn.util.NbtConvertible
import net.minecraft.client.item.TooltipData
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound

data class Trade(var selling: ItemStack, var price: ItemStack) : NbtConvertible, TooltipData {
    private val listeners: MutableList<TradeListener> = ArrayList()

    fun isEmpty() = selling.isEmpty || price.isEmpty

    override fun readNbt(nbt: NbtCompound) {
        selling = ItemStack.fromNbt(nbt.getCompound(NBT_SELLING))
        price = ItemStack.fromNbt(nbt.getCompound(NBT_PRICE))
    }

    override fun writeNbt(nbt: NbtCompound) = nbt.apply {
        put(NBT_SELLING, selling.writeNbt(NbtCompound()))
        put(NBT_PRICE, price.writeNbt(NbtCompound()))
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

    companion object {
        const val NBT_SELLING = "Selling"
        const val NBT_PRICE = "Price"
    }

    fun interface TradeListener {
        fun onTradeChanged(trade: Trade)
    }
}
