package juuxel.adorn.block.entity

import juuxel.adorn.block.AdornBlockEntities
import juuxel.adorn.menu.DrawerMenu
import net.minecraft.entity.player.PlayerInventory

class DrawerBlockEntity : BaseInventoryBlockEntity(AdornBlockEntities.DRAWER.get(), 15) {
    override fun createScreenHandler(syncId: Int, playerInv: PlayerInventory) =
        DrawerMenu(syncId, playerInv, this)
}
