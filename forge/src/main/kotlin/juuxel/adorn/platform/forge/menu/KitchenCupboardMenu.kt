package juuxel.adorn.platform.forge.menu

import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandlerContext
import net.minecraftforge.network.IContainerFactory

class KitchenCupboardMenu(
    syncId: Int,
    playerInventory: PlayerInventory,
    container: Inventory = SimpleInventory(5 * 3),
    context: ScreenHandlerContext
) : SimpleMenu(AdornMenusForge.KITCHEN_CUPBOARD, syncId, 5 to 3, container, playerInventory, context) {
    companion object {
        private val DIMENSIONS = 5 to 3
        val FACTORY: IContainerFactory<KitchenCupboardMenu> = IContainerFactory(this::load)

        private fun load(syncId: Int, playerInventory: PlayerInventory, buf: PacketByteBuf): KitchenCupboardMenu {
            val pos = buf.readBlockPos()
            val context = ScreenHandlerContext.create(playerInventory.player.world, pos)
            return KitchenCupboardMenu(syncId, playerInventory, SimpleInventory(DIMENSIONS.first * DIMENSIONS.second), context)
        }
    }
}
