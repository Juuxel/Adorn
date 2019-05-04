package juuxel.adorn.trading

import juuxel.adorn.util.NbtConvertible
import juuxel.adorn.util.Observable
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag

data class Trade(var item: ItemStack, var price: ItemStack) : Observable<Trade>(), NbtConvertible {
    override fun fromTag(tag: CompoundTag) {
        item = ItemStack.fromTag(tag.getCompound("Item"))
        price = ItemStack.fromTag(tag.getCompound("Price"))
    }

    override fun toTag(tag: CompoundTag) = tag.apply {
        put("Item", item.toTag(CompoundTag()))
        put("Price", price.toTag(CompoundTag()))
    }

    fun createInventory(forOwner: Boolean) = TradeInventory(this, forOwner)
}
