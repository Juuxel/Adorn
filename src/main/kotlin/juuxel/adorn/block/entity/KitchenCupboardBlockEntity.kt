package juuxel.adorn.block.entity

import juuxel.adorn.block.AdornBlockEntities
import juuxel.adorn.gui.controller.KitchenCupboardController
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext

class KitchenCupboardBlockEntity : BaseInventoryBlockEntity(AdornBlockEntities.KITCHEN_CUPBOARD, 15) {
    override fun createContainer(syncId: Int, playerInv: PlayerInventory) =
        KitchenCupboardController(syncId, playerInv, ScreenHandlerContext.create(world, pos), displayName)
}
