package juuxel.adorn.block.entity

import juuxel.adorn.block.AdornBlockEntities
import juuxel.adorn.platform.MenuBridge
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.util.math.BlockPos

abstract class DrawerBlockEntity(pos: BlockPos, state: BlockState) :
    BaseInventoryBlockEntity(AdornBlockEntities.DRAWER, pos, state, 15) {
    override fun createScreenHandler(syncId: Int, playerInv: PlayerInventory) =
        MenuBridge.createDrawerMenu(syncId, playerInv, ScreenHandlerContext.create(world, pos))
}
