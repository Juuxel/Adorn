package juuxel.adorn.block.entity

import juuxel.adorn.menu.ContainerBlockMenu
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.entity.ViewerCountManager
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

/**
 * A simple container block entity with a menu.
 * These block entities also send game events when they are opened/closed.
 */
abstract class SimpleContainerBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState, size: Int) :
    BaseContainerBlockEntity(type, pos, state, size) {
    @Suppress("LeakingThis")
    private val viewerCountManager = createViewerCountManager()

    protected open fun createViewerCountManager(): ViewerCountManager =
        SimpleViewerCountManager(this)

    override fun onOpen(player: PlayerEntity) {
        viewerCountManager.openContainer(player, world, pos, cachedState)
    }

    override fun onClose(player: PlayerEntity) {
        viewerCountManager.closeContainer(player, world, pos, cachedState)
    }

    fun onScheduledTick() {
        if (!removed) {
            viewerCountManager.updateViewerCount(world, pos, cachedState)
        }
    }

    protected open class SimpleViewerCountManager(private val entity: SimpleContainerBlockEntity) : ViewerCountManager() {
        override fun onContainerOpen(world: World, pos: BlockPos, state: BlockState) {}
        override fun onContainerClose(world: World, pos: BlockPos, state: BlockState) {}
        override fun onViewerCountUpdate(world: World, pos: BlockPos, state: BlockState, oldViewerCount: Int, newViewerCount: Int) {}

        override fun isPlayerViewing(player: PlayerEntity): Boolean {
            val menu = player.menu
            return menu is ContainerBlockMenu && menu.inventory === entity
        }
    }
}
