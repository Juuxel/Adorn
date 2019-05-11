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
    private var listeners: MutableList<InventoryListener>? = null
    private val items: DefaultedList<ItemStack> = DefaultedList.create(invSize, ItemStack.EMPTY)
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
        var remainingAmount = stack.amount

        for (invStack in items) {
            if (invStack.isEqualIgnoreTags(stack)) {
                remainingAmount -= invStack.amount
                if (remainingAmount <= 0) return true
            }
        }

        return false
    }

    /**
     * Tries to remove the [stack] from this inventory. Ignores NBT, durability and tags.
     *
     * @return `true` if extracted
     */
    fun tryExtract(stack: ItemStack): Boolean {
        var remainingAmount = stack.amount

        for (invStack in items) {
            if (invStack.isEqualIgnoreTags(stack)) {
                invStack.subtractAmount(min(invStack.amount, remainingAmount))
                remainingAmount -= invStack.amount
                if (remainingAmount <= 0) return true
            }
        }

        return false
    }

    /**
     * Checks if the [stack] can be inserted to this inventory. Ignores NBT, durability and tags.
     */
    fun canInsert(stack: ItemStack): Boolean {
        var remainingAmount = stack.amount

        for ((slot, invStack) in items.withIndex()) {
            if (invStack.isEqualIgnoreTags(stack) && invStack.amount < invStack.maxAmount) {
                val insertionAmount = min(invStack.maxAmount - invStack.amount, remainingAmount)
                remainingAmount -= insertionAmount
                if (remainingAmount <= 0) return true
            } else if (invStack.isEmpty) {
                return true
            }
        }

        return false
    }

    /**
     * Tries to insert the [stack] to this inventory. Ignores NBT, durability and tags.
     *
     * @return `true` if inserted
     */
    fun tryInsert(stack: ItemStack): Boolean {
        var remainingAmount = stack.amount

        for ((slot, invStack) in items.withIndex()) {
            if (invStack.isEqualIgnoreTags(stack) && invStack.amount < invStack.maxAmount) {
                val insertionAmount = min(invStack.maxAmount - invStack.amount, remainingAmount)
                remainingAmount -= insertionAmount
                invStack.addAmount(insertionAmount)
                if (remainingAmount <= 0) return true
            } else if (invStack.isEmpty) {
                items[slot] = stack.copy()
                return true
            }
        }

        return false
    }

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

    override fun takeInvStack(slot: Int, amount: Int) =
        Inventories.splitStack(items, slot, amount).also {
            if (!it.isEmpty) {
                markDirty()
            }
        }

    override fun isInvEmpty(): Boolean {
        for (stack in items) {
            if (!stack.isEmpty) {
                return false
            }
        }

        return true
    }

    //-----------
    // Listeners
    //-----------

    override fun markDirty() {
        listeners?.forEach { it.onInvChange(this) }
    }

    fun addListener(listener: InventoryListener) {
        if (listeners == null) {
            listeners = arrayListOf(listener)
        } else {
            listeners!!.add(listener)
        }
    }

    inline fun addListener(crossinline block: (Inventory) -> Unit) =
        addListener(InventoryListener {
            block(it)
        })
}
