package juuxel.adorn.block.entity

import juuxel.adorn.block.AdornBlockEntities
import juuxel.adorn.menu.KitchenCupboardMenu
import net.minecraft.entity.player.PlayerInventory

class KitchenCupboardBlockEntity : BaseInventoryBlockEntity(AdornBlockEntities.KITCHEN_CUPBOARD.get(), 15) {
    override fun createMenu(syncId: Int, playerInv: PlayerInventory) =
        KitchenCupboardMenu(syncId, playerInv, this)
}
