package juuxel.adorn.menu

import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.menu.MenuContext
import net.minecraft.network.PacketByteBuf

class KitchenCupboardMenu(
    syncId: Int,
    playerInventory: PlayerInventory,
    container: Inventory = SimpleInventory(DIMENSIONS.size),
    context: MenuContext
) : PalettedMenu(AdornMenus.KITCHEN_CUPBOARD, syncId, DIMENSIONS, container, playerInventory, context) {
    companion object {
        private val DIMENSIONS = 5 by 3

        fun load(syncId: Int, playerInventory: PlayerInventory, buf: PacketByteBuf): KitchenCupboardMenu {
            val pos = buf.readBlockPos()
            val context = MenuContext.create(playerInventory.player.world, pos)
            return KitchenCupboardMenu(syncId, playerInventory, SimpleInventory(DIMENSIONS.size), context)
        }
    }
}
