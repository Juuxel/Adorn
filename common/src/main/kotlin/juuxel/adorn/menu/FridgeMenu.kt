package juuxel.adorn.menu

import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory

class FridgeMenu(syncId: Int, playerInventory: PlayerInventory, container: Inventory) :
    SimpleMenu(AdornMenus.FRIDGE, syncId, DIMENSIONS, container, playerInventory) {
    constructor(syncId: Int, playerInventory: PlayerInventory) :
        this(syncId, playerInventory, SimpleInventory(DIMENSIONS.size))

    companion object {
        private val DIMENSIONS = 9 by 3
    }
}
