package juuxel.adorn.menu

import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandlerContext

class DrawerMenu(
    syncId: Int,
    playerInventory: PlayerInventory,
    container: Inventory,
    context: ScreenHandlerContext
) : SimpleMenu(AdornMenus.DRAWER, syncId, DIMENSIONS, container, playerInventory, context) {
    companion object {
        private val DIMENSIONS = 5 to 3

        fun load(syncId: Int, playerInventory: PlayerInventory, buf: PacketByteBuf): DrawerMenu {
            val pos = buf.readBlockPos()
            val context = ScreenHandlerContext.create(playerInventory.player.world, pos)
            return DrawerMenu(syncId, playerInventory, SimpleInventory(DIMENSIONS.first * DIMENSIONS.second), context)
        }
    }
}
