package juuxel.adorn.menu

import juuxel.adorn.block.AdornBlocks
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.menu.Menu
import net.minecraft.menu.MenuContext
import net.minecraft.world.World

class FurnitureWorkbenchMenu(
    syncId: Int, playerInventory: PlayerInventory,
    private val context: MenuContext = MenuContext.EMPTY
) : Menu(AdornMenus.FURNITURE_WORKBENCH, syncId) {
    private val world: World = playerInventory.player.world

    fun addCuboid() {
        if (!world.isClient) {
            throw IllegalStateException("addCuboid must only be called on the client!")
        }
    }

    override fun quickMove(player: PlayerEntity, slot: Int): ItemStack {
        TODO("Not yet implemented")
    }

    override fun canUse(player: PlayerEntity): Boolean =
        canUse(context, player, AdornBlocks.FURNITURE_WORKBENCH)
}
