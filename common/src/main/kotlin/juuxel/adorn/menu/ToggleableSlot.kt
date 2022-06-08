package juuxel.adorn.menu

import net.minecraft.inventory.Inventory
import net.minecraft.menu.Slot

open class ToggleableSlot(inventory: Inventory, index: Int, x: Int, y: Int) : Slot(inventory, index, x, y) {
    var enabled: Boolean = true

    override fun isEnabled(): Boolean = enabled
}
