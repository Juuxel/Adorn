package juuxel.adorn.block.entity

import juuxel.adorn.block.AdornBlockEntities
import juuxel.adorn.menu.FurnitureWorkbenchMenu
import juuxel.adorn.util.menuContextOf
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.menu.Menu
import net.minecraft.menu.NamedMenuFactory
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos

class FurnitureWorkbenchBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(AdornBlockEntities.FURNITURE_WORKBENCH, pos, state), NamedMenuFactory {
    override fun createMenu(syncId: Int, inv: PlayerInventory, player: PlayerEntity): Menu =
        FurnitureWorkbenchMenu(syncId, inv, menuContextOf(this))

    override fun getDisplayName(): Text =
        cachedState.block.name
}
