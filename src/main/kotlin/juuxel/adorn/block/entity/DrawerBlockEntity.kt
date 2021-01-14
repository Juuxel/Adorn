package juuxel.adorn.block.entity

import juuxel.adorn.block.AdornBlockEntities
import juuxel.adorn.menu.DrawerMenu
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.menu.MenuContext

class DrawerBlockEntity : BaseInventoryBlockEntity(AdornBlockEntities.DRAWER, 15) {
    override fun createMenu(syncId: Int, playerInv: PlayerInventory) =
        DrawerMenu(syncId, playerInv, MenuContext.create(world, pos))
}
