package juuxel.adorn.util

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.InventoryListener
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.DefaultedList

class InventoryComponent(private val invSize: Int) : Inventory, NbtConvertible {
    private var listeners: MutableList<InventoryListener>? = null
    private val items: DefaultedList<ItemStack> = DefaultedList.create(invSize, ItemStack.EMPTY)
    val sidedInventory: SidedInventory by lazy { SidedInventoryImpl(this) }

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

    override fun takeInvStack(p0: Int, p1: Int) =
        Inventories.splitStack(items, p0, p1).also {
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
