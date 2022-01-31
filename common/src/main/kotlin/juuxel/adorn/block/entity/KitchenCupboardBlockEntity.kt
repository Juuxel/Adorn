package juuxel.adorn.block.entity

import juuxel.adorn.block.AdornBlockEntities
import juuxel.adorn.menu.KitchenCupboardMenu
import juuxel.adorn.util.menuContextOf
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.math.BlockPos

class KitchenCupboardBlockEntity(pos: BlockPos, state: BlockState) :
    SimpleContainerBlockEntity(AdornBlockEntities.KITCHEN_CUPBOARD, pos, state, 15) {
    override fun createMenu(syncId: Int, playerInv: PlayerInventory) =
        KitchenCupboardMenu(syncId, playerInv, this, menuContextOf(this))
}
