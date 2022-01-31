package juuxel.adorn.block.entity

import juuxel.adorn.block.AdornBlockEntities
import juuxel.adorn.menu.DrawerMenu
import juuxel.adorn.util.menuContextOf
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.math.BlockPos

class DrawerBlockEntity(pos: BlockPos, state: BlockState) :
    SimpleContainerBlockEntity(AdornBlockEntities.DRAWER, pos, state, 15) {
    override fun createMenu(syncId: Int, playerInv: PlayerInventory) =
        DrawerMenu(syncId, playerInv, this, menuContextOf(this))
}
