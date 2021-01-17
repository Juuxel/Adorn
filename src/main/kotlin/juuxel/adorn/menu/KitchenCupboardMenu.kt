package juuxel.adorn.menu

import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory

class KitchenCupboardMenu(
    syncId: Int,
    playerInventory: PlayerInventory,
    container: Inventory = SimpleInventory(5 * 3)
) : SimpleMenu(AdornMenus.KITCHEN_CUPBOARD.get(), syncId, 5 to 3, container, playerInventory)
