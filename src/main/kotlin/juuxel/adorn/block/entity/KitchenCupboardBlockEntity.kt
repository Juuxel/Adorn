package juuxel.adorn.block.entity

import juuxel.adorn.block.KitchenCupboardBlock
import juuxel.adorn.gui.controller.KitchenCupboardController
import net.minecraft.container.BlockContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory

class KitchenCupboardBlockEntity : BaseInventoryBlockEntity(KitchenCupboardBlock.BLOCK_ENTITY_TYPE, 15) {
    override fun createMenu(syncId: Int, playerInv: PlayerInventory, player: PlayerEntity?) =
        KitchenCupboardController(syncId, playerInv, BlockContext.create(world, pos))
}
