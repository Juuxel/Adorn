package juuxel.adorn.block.entity

import juuxel.adorn.block.AdornBlockEntities
import juuxel.adorn.menu.FridgeMenu
import net.minecraft.block.BlockState
import net.minecraft.block.entity.ChestLidAnimator
import net.minecraft.block.entity.LidOpenable
import net.minecraft.block.entity.ViewerCountManager
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.menu.Menu
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class FridgeBlockEntity(pos: BlockPos, state: BlockState) :
    SimpleContainerBlockEntity(AdornBlockEntities.FRIDGE, pos, state, CONTAINER_SIZE), LidOpenable {
    private val lidAnimator = ChestLidAnimator()

    override fun getAnimationProgress(tickDelta: Float): Float =
        lidAnimator.getProgress(tickDelta)

    override fun createMenu(syncId: Int, inv: PlayerInventory): Menu =
        FridgeMenu(syncId, inv, this)

    override fun onSyncedBlockEvent(type: Int, data: Int): Boolean {
        if (type == VIEWER_COUNT_EVENT) {
            lidAnimator.setOpen(data > 0)
            return true
        }

        return super.onSyncedBlockEvent(type, data)
    }

    override fun createViewerCountManager(): ViewerCountManager =
        FridgeViewerCountManager(this)

    private class FridgeViewerCountManager(entity: FridgeBlockEntity) : SimpleViewerCountManager(entity) {
        override fun onViewerCountUpdate(world: World, pos: BlockPos, state: BlockState, oldViewerCount: Int, newViewerCount: Int) {
            world.addSyncedBlockEvent(pos, state.block, VIEWER_COUNT_EVENT, newViewerCount)
        }
    }

    companion object {
        const val CONTAINER_SIZE = 9 * 3
        private const val VIEWER_COUNT_EVENT = 1

        fun clientTick(world: World, pos: BlockPos, state: BlockState, fridge: FridgeBlockEntity) {
            fridge.lidAnimator.step()
        }
    }
}
