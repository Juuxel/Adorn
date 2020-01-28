package juuxel.adorn.block.entity

import juuxel.adorn.block.AdornBlockEntities
import juuxel.adorn.gui.controller.DrawerController
import net.minecraft.container.BlockContext
import net.minecraft.entity.player.PlayerInventory

class DrawerBlockEntity : BaseInventoryBlockEntity(AdornBlockEntities.DRAWER, 15) {
    override fun createContainer(syncId: Int, playerInv: PlayerInventory) =
        DrawerController(syncId, playerInv, BlockContext.create(world, pos), displayName)
}
