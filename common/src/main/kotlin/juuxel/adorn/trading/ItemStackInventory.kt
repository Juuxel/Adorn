package juuxel.adorn.trading

import juuxel.adorn.util.InventoryComponent
import net.minecraft.item.ItemStack

class ItemStackInventory(stack: ItemStack, size: Int) : InventoryComponent(size) {
    var stack: ItemStack = stack
        set(value) {
            if (!ItemStack.areEqual(field, value)) {
                field = value.copy()
                reloadFromStack(field)
            }
        }

    init {
        reloadFromStack(stack)
        addListener {
            writeNbt(this.stack.getOrCreateSubNbt(ITEMS_KEY))
        }
    }

    private fun reloadFromStack(stack: ItemStack) {
        // Clear existing items and replace them with the new stack's contents
        clear()
        stack.getSubNbt(ITEMS_KEY)?.let { itemsNbt ->
            readNbt(itemsNbt)
        }
    }

    companion object {
        private const val ITEMS_KEY = "Items"

        fun ofInventoryStack(inventory: InventoryComponent, slot: Int, size: Int): ItemStackInventory {
            val stackInventory = ItemStackInventory(inventory.getStack(slot), size)
            inventory.addListener {
                stackInventory.stack = it.getStack(slot)
            }
            stackInventory.addListener {
                inventory.markDirty()
            }
            return stackInventory
        }
    }
}
