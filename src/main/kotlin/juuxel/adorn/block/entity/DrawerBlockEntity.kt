package juuxel.adorn.block.entity

import juuxel.adorn.block.DrawerBlock
import juuxel.adorn.gui.controller.DrawerController
import net.minecraft.container.BlockContext
import net.minecraft.entity.player.PlayerInventory

class DrawerBlockEntity : BaseInventoryBlockEntity(DrawerBlock.BLOCK_ENTITY_TYPE, 15) {
    override fun createContainer(syncId: Int, playerInv: PlayerInventory) =
        DrawerController(syncId, playerInv, BlockContext.create(world, pos), displayName)
}
