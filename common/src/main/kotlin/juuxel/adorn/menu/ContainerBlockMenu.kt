package juuxel.adorn.menu

import net.minecraft.inventory.Inventory
import net.minecraft.menu.MenuContext

interface ContainerBlockMenu {
    val inventory: Inventory
    val context: MenuContext
}
