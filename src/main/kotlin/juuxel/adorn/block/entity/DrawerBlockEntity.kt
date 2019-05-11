package juuxel.adorn.block.entity

import juuxel.adorn.block.DrawerBlock
import juuxel.adorn.gui.controller.DrawerController
import net.minecraft.container.BlockContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory

class DrawerBlockEntity : BaseInventoryBlockEntity(DrawerBlock.BLOCK_ENTITY_TYPE, 15) {
    override fun createMenu(syncId: Int, playerInv: PlayerInventory, player: PlayerEntity?) =
        DrawerController(syncId, playerInv, BlockContext.create(world, pos))
}
