package juuxel.adorn.block.entity

import juuxel.adorn.block.AdornBlockEntities
import juuxel.adorn.menu.KitchenCupboardMenu
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.menu.MenuContext

class KitchenCupboardBlockEntity : BaseInventoryBlockEntity(AdornBlockEntities.KITCHEN_CUPBOARD, 15) {
    override fun createMenu(syncId: Int, playerInv: PlayerInventory) =
        KitchenCupboardMenu(syncId, playerInv, MenuContext.create(world, pos))
}
