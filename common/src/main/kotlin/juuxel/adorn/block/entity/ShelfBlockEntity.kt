package juuxel.adorn.block.entity

import juuxel.adorn.block.AdornBlockEntities
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.math.BlockPos

abstract class ShelfBlockEntity(pos: BlockPos, state: BlockState) :
    BaseContainerBlockEntity(AdornBlockEntities.SHELF, pos, state, 2) {
    // No menus for shelves
    override fun createScreenHandler(syncId: Int, playerInv: PlayerInventory?) = null

    override fun getMaxCountPerStack() = 1
}
