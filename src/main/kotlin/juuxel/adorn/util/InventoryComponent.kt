package juuxel.adorn.util

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.InventoryListener
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.DefaultedList
import kotlin.math.min
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

open class InventoryComponent(private val invSize: Int) : Inventory, NbtConvertible {
    private val listeners: MutableList<InventoryListener> = ArrayList()
    private val items: DefaultedList<ItemStack> = DefaultedList.ofSize(invSize, ItemStack.EMPTY)
    val sidedInventory: SidedInventory by lazy { SidedInventoryImpl(this) }

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
            if (invStack.isItemEqual(stack)) {
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
            if (invStack.isItemEqual(stack) && invStack.tag == stack.tag) {
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
            if (invStack.isItemEqual(stack) && invStack.count < invStack.maxCount && stack.tag == invStack.tag) {
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
            if (invStack.isItemEqual(stack) && invStack.count < invStack.maxCount && invStack.tag == stack.tag) {
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
    fun getAmountWithNbt(stack: ItemStack): Int = items.map {
        if (stack.item == it.item && stack.tag == it.tag)
            it.count
        else 0
    }.sum()

    fun slot(slot: Int) = object : ReadWriteProperty<Any?, ItemStack> {
        override fun getValue(thisRef: Any?, property: KProperty<*>) = getInvStack(slot)
        override fun setValue(thisRef: Any?, property: KProperty<*>, value: ItemStack) = setInvStack(slot, value)
    }

    //-----
    // NBT
    //-----

    override fun toTag(tag: CompoundTag): CompoundTag = tag.apply {
        Inventories.toTag(tag, items)
    }

    override fun fromTag(tag: CompoundTag) {
        Inventories.fromTag(tag, items)
    }

    //-------------------------------
    // Inventory management/transfer
    //-------------------------------

    override fun getInvStack(slot: Int) = items[slot]

    override fun clear() {
        items.clear()
        markDirty()
    }

    override fun setInvStack(slot: Int, stack: ItemStack) {
        items[slot] = stack
    }

    override fun removeInvStack(slot: Int) =
        Inventories.removeStack(items, slot)

    override fun canPlayerUseInv(player: PlayerEntity?) = true

    override fun getInvSize() = invSize

    override fun takeInvStack(slot: Int, count: Int) =
        Inventories.splitStack(items, slot, count).also {
            if (!it.isEmpty) {
                markDirty()
            }
        }

    override fun isInvEmpty(): Boolean = isInvEmpty(items)

    //-----------
    // Listeners
    //-----------

    override fun markDirty() {
        listeners.forEach { it.onInvChange(this) }
    }

    fun addListener(listener: InventoryListener) {
        listeners += listener
    }

    inline fun addListener(crossinline block: (Inventory) -> Unit) =
        addListener(InventoryListener {
            block(it)
        })

    companion object {
        fun isInvEmpty(stacks: List<ItemStack>) = stacks.none { !it.isEmpty }
    }
}
