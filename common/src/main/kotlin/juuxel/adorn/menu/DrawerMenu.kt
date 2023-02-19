package juuxel.adorn.menu

import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.menu.MenuContext
import net.minecraft.network.PacketByteBuf

class DrawerMenu(
    syncId: Int,
    playerInventory: PlayerInventory,
    container: Inventory,
    context: MenuContext
) : PalettedMenu(AdornMenus.DRAWER, syncId, DIMENSIONS, container, playerInventory, context) {
    companion object {
        private val DIMENSIONS = 5 by 3

        fun load(syncId: Int, playerInventory: PlayerInventory, buf: PacketByteBuf): DrawerMenu {
            val pos = buf.readBlockPos()
            val context = MenuContext.create(playerInventory.player.world, pos)
            return DrawerMenu(syncId, playerInventory, SimpleInventory(DIMENSIONS.size), context)
        }
    }
}
