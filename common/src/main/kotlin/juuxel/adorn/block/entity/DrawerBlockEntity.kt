package juuxel.adorn.block.entity

import juuxel.adorn.block.AdornBlockEntities
import juuxel.adorn.platform.PlatformBridges
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.util.math.BlockPos

open class DrawerBlockEntity(pos: BlockPos, state: BlockState) :
    SimpleContainerBlockEntity(AdornBlockEntities.DRAWER, pos, state, 15) {
    override fun createScreenHandler(syncId: Int, playerInv: PlayerInventory) =
        PlatformBridges.menus.drawer(syncId, playerInv, ScreenHandlerContext.create(world, pos))
}
