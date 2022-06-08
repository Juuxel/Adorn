package juuxel.adorn.util

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack

class ChainedInventory(private val parts: List<Inventory>) : Inventory {
    private fun findInventoryAndIndex(slot: Int): Pair<Inventory, Int> {
        var index = slot

        for (part in parts) {
            if (index in 0 until part.size()) {
                return part to index
            }

            index -= part.size()
        }

        throw IndexOutOfBoundsException("Slot $slot for inventory $this")
    }

    private inline fun <R> withPart(slot: Int, fn: (Inventory, Int) -> R): R {
        val (inventory, index) = findInventoryAndIndex(slot)
        return fn(inventory, index)
    }

    override fun size(): Int = parts.sumOf { it.size() }
    override fun isEmpty(): Boolean = parts.all { it.isEmpty }
    override fun getStack(slot: Int): ItemStack = withPart(slot, Inventory::getStack)
    override fun removeStack(slot: Int): ItemStack = withPart(slot, Inventory::removeStack)
    override fun removeStack(slot: Int, amount: Int): ItemStack =
        withPart(slot) { part, index -> part.removeStack(index, amount) }
    override fun setStack(slot: Int, stack: ItemStack) =
        withPart(slot) { part, index -> part.setStack(index, stack) }
    override fun canPlayerUse(player: PlayerEntity): Boolean = parts.all { it.canPlayerUse(player) }

    override fun markDirty() {
        for (part in parts) {
            part.markDirty()
        }
    }

    override fun clear() {
        for (part in parts) {
            part.clear()
        }
    }
}
