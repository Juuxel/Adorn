package juuxel.adorn.menu

import net.minecraft.inventory.Inventory
import net.minecraft.screen.ScreenHandlerContext

interface ContainerBlockMenu {
    val inventory: Inventory
    val context: ScreenHandlerContext
}
