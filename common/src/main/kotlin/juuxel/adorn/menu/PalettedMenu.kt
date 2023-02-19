package juuxel.adorn.menu

import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.menu.MenuContext
import net.minecraft.menu.MenuType

abstract class PalettedMenu(
    type: MenuType<*>,
    syncId: Int,
    dimensions: ContainerDimensions,
    inventory: Inventory,
    playerInventory: PlayerInventory,
    override val context: MenuContext
) : SimpleMenu(type, syncId, dimensions, inventory, playerInventory), MenuWithContext
