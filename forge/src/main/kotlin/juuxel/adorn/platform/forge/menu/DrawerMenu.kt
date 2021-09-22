package juuxel.adorn.platform.forge.menu

import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory

class DrawerMenu(
    syncId: Int,
    playerInventory: PlayerInventory,
    container: Inventory = SimpleInventory(DIMENSIONS.first * DIMENSIONS.second)
) : SimpleMenu(AdornMenus.DRAWER, syncId, DIMENSIONS, container, playerInventory) {
    companion object {
        private val DIMENSIONS = 5 to 3
    }
}
