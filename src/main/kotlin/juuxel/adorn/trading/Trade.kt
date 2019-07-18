package juuxel.adorn.trading

import juuxel.adorn.util.NbtConvertible
import juuxel.adorn.util.Observable
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag

data class Trade(var selling: ItemStack, var price: ItemStack) : Observable<Trade>(), NbtConvertible {
    fun isEmpty() = selling.isEmpty || price.isEmpty

    override fun fromTag(tag: CompoundTag) {
        selling = ItemStack.fromTag(tag.getCompound("Selling"))
        price = ItemStack.fromTag(tag.getCompound("Price"))
    }

    override fun toTag(tag: CompoundTag) = tag.apply {
        put("Selling", selling.toTag(CompoundTag()))
        put("Price", price.toTag(CompoundTag()))
    }

    /**
     * Creates a modifiable inventory for this trade.
     */
    fun createInventory() = TradeInventory(this)
}
