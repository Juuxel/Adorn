package juuxel.adorn.util

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack

class CompoundInventory(private vararg val inventories: Inventory) : Inventory {
    private val size = inventories.map(Inventory::getInvSize).sum()
    private val slotToInvAndSlot: Int2ObjectMap<Pair<Inventory, Int>>

    init {
        val map = Int2ObjectArrayMap<Pair<Inventory, Int>>()
        var offset = 0
        for (inv in inventories) {
            for (invSlot in 0 until inv.invSize) {
                map[offset + invSlot] = inv to invSlot
            }

            offset += inv.invSize
        }

        slotToInvAndSlot = Int2ObjectMaps.unmodifiable(map)
    }

    private inline fun <R> onInvAndSlot(slot: Int, block: (inv: Inventory, invSlot: Int) -> R): R =
        slotToInvAndSlot[slot].let { (inv, invSlot) -> block(inv, invSlot) }

    override fun getInvStack(slot: Int) = onInvAndSlot(slot, Inventory::getInvStack)

    override fun markDirty() = inventories.forEach(Inventory::markDirty)

    override fun clear() = inventories.forEach(Inventory::clear)

    override fun setInvStack(slot: Int, stack: ItemStack): Unit = onInvAndSlot(slot) { inv, invSlot ->
        inv.setInvStack(invSlot, stack)
    }

    override fun removeInvStack(slot: Int) = onInvAndSlot(slot, Inventory::removeInvStack)

    override fun canPlayerUseInv(p0: PlayerEntity?) = true

    override fun getInvSize() = size

    override fun takeInvStack(slot: Int, amount: Int) = onInvAndSlot(slot) { inv, invSlot ->
        inv.takeInvStack(invSlot, amount)
    }

    override fun isInvEmpty() = inventories.any(Inventory::isInvEmpty)
}
