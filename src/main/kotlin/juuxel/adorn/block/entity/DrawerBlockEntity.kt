package juuxel.adorn.block.entity

import juuxel.adorn.block.AdornBlockEntities
import juuxel.adorn.menu.DrawerMenu
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext

class DrawerBlockEntity : BaseInventoryBlockEntity(AdornBlockEntities.DRAWER, 15) {
    override fun createScreenHandler(syncId: Int, playerInv: PlayerInventory) =
        DrawerMenu(syncId, playerInv, ScreenHandlerContext.create(world, pos))
}
