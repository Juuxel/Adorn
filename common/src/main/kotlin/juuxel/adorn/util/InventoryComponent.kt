package juuxel.adorn.util

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.InventoryChangedListener
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.collection.DefaultedList
import kotlin.math.min

open class InventoryComponent(private val invSize: Int) : Inventory, NbtConvertible {
    private val listeners: MutableList<InventoryChangedListener> = ArrayList()
    private val items: DefaultedList<ItemStack> = DefaultedList.ofSize(invSize, ItemStack.EMPTY)

    private constructor(items: DefaultedList<ItemStack>) : this(items.size) {
        for ((i, item) in items.withIndex()) {
            this.items[i] = item
        }
    }

    /**
     * Creates a copy of this inventory, not retaining any listeners.
     */
    fun copy(): InventoryComponent = InventoryComponent(items)

    /**
     * Checks if the [stack] can be extracted from this inventory. Ignores NBT, durability and tags.
     */
    fun canExtract(stack: ItemStack): Boolean {
        var remainingAmount = stack.count

        for (invStack in items) {
            if (ItemStack.areItemsEqual(invStack, stack)) {
                remainingAmount -= invStack.count
                if (remainingAmount <= 0) return true
            }
        }

        return false
    }

    /**
     * Tries to remove the [stack] from this inventory. Ignores tags.
     *
     * @return `true` if extracted
     */
    fun tryExtract(stack: ItemStack): Boolean {
        var remainingAmount = stack.count

        for (invStack in items) {
            if (ItemStack.areItemsEqual(invStack, stack) && invStack.nbt == stack.nbt) {
                val invStackAmount = invStack.count
                invStack.decrement(min(invStackAmount, remainingAmount))
                remainingAmount -= invStackAmount
                if (remainingAmount <= 0) return true
            }
        }

        return false
    }

    /**
     * Checks if the [stack] can be inserted to this inventory. Ignores tags.
     */
    fun canInsert(stack: ItemStack): Boolean {
        var remainingAmount = stack.count

        for (invStack in items) {
            if (ItemStack.areItemsEqual(invStack, stack) && invStack.count < invStack.maxCount && stack.nbt == invStack.nbt) {
                val insertionAmount = min(invStack.maxCount - invStack.count, remainingAmount)
                remainingAmount -= insertionAmount
                if (remainingAmount <= 0) return true
            } else if (invStack.isEmpty) {
                return true
            }
        }

        return false
    }

    /**
     * Tries to insert the [stack] to this inventory. Ignores tags.
     *
     * @return `true` if inserted
     */
    fun tryInsert(stack: ItemStack): Boolean {
        var remainingAmount = stack.count

        for ((slot, invStack) in items.withIndex()) {
            if (ItemStack.areItemsEqual(invStack, stack) && invStack.count < invStack.maxCount && invStack.nbt == stack.nbt) {
                val insertionAmount = min(invStack.maxCount - invStack.count, remainingAmount)
                remainingAmount -= insertionAmount
                invStack.increment(insertionAmount)
                if (remainingAmount <= 0) return true
            } else if (invStack.isEmpty) {
                items[slot] = stack.copy()
                return true
            }
        }

        return false
    }

    /**
     * Gets the count of items with the same item and NBT as the [stack].
     * Ignores the stack's count.
     */
    fun getAmountWithNbt(stack: ItemStack): Int = items.sumOf {
        if (stack.item == it.item && stack.nbt == it.nbt) {
            it.count
        } else {
            0
        }
    }

    // -----
    // NBT
    // -----

    override fun writeNbt(nbt: NbtCompound): NbtCompound = nbt.apply {
        Inventories.writeNbt(nbt, items)
    }

    override fun readNbt(nbt: NbtCompound) {
        Inventories.readNbt(nbt, items)
    }

    // -------------------------------
    // Inventory management/transfer
    // -------------------------------

    override fun getStack(slot: Int) = items[slot]

    override fun clear() {
        items.clear()
        markDirty()
    }

    override fun setStack(slot: Int, stack: ItemStack) {
        items[slot] = stack
    }

    override fun removeStack(slot: Int) =
        Inventories.removeStack(items, slot)

    override fun canPlayerUse(player: PlayerEntity?) = true

    override fun size() = invSize

    override fun removeStack(slot: Int, count: Int) =
        Inventories.splitStack(items, slot, count).also {
            if (!it.isEmpty) {
                markDirty()
            }
        }

    override fun isEmpty(): Boolean = items.all { it.isEmpty }

    // -----------
    // Listeners
    // -----------

    override fun markDirty() {
        listeners.forEach { it.onInventoryChanged(this) }
    }

    fun addListener(listener: InventoryChangedListener) {
        listeners += listener
    }
}
