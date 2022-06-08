package juuxel.adorn.menu

import net.minecraft.inventory.Inventory
import net.minecraft.menu.Menu

class SlotGroup(
    inventory: Inventory,
    private val width: Int,
    private val height: Int,
    slotFactory: (Inventory, Int, Int, Int) -> ToggleableSlot,
    startIndex: Int = 0
) : Iterable<ToggleableSlot> {
    private val slots: List<ToggleableSlot> = (0 until width * height).map {
        val slot = slotFactory(inventory, startIndex + it, 0, 0)
        slot.enabled = false
        slot
    }

    fun show() {
        for (slot in this) {
            slot.enabled = true
        }
    }

    fun hide() {
        for (slot in this) {
            slot.enabled = false
        }
    }

    fun addTo(menu: Menu) {
        for (slot in this) {
            menu.addSlot(slot)
        }
    }

    fun move(x: Int, y: Int) {
        for (row in 0 until height) {
            for (column in 0 until width) {
                val index = row * width + column
                val slot = slots[index]
                slot.x = x + column * 18
                slot.y = y + row * 18
            }
        }
    }

    override fun iterator(): Iterator<ToggleableSlot> = slots.iterator()
}
