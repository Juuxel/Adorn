package juuxel.adorn.block.entity

import juuxel.adorn.block.AdornBlockEntities
import juuxel.adorn.menu.KitchenCupboardMenu
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext

class KitchenCupboardBlockEntity : BaseInventoryBlockEntity(AdornBlockEntities.KITCHEN_CUPBOARD, 15) {
    override fun createScreenHandler(syncId: Int, playerInv: PlayerInventory) =
        KitchenCupboardMenu(syncId, playerInv, ScreenHandlerContext.create(world, pos))
}
