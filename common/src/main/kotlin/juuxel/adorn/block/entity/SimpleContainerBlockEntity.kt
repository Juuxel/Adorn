package juuxel.adorn.block.entity

import juuxel.adorn.menu.ContainerBlockMenu
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.entity.ChestStateManager
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

/**
 * A simple container block entity with a menu.
 * These block entities also send game events when they are opened/closed.
 */
abstract class SimpleContainerBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState, size: Int) :
    BaseContainerBlockEntity(type, pos, state, size),
    ExtendedMenuFactory {
    private val stateManager: ChestStateManager = object : ChestStateManager() {
        override fun onChestOpened(world: World, pos: BlockPos, state: BlockState) {}
        override fun onChestClosed(world: World, pos: BlockPos, state: BlockState) {}
        override fun onInteracted(
            world: World, pos: BlockPos, state: BlockState,
            oldViewerCount: Int, newViewerCount: Int
        ) {}

        override fun isPlayerViewing(player: PlayerEntity): Boolean {
            val menu = player.currentScreenHandler
            return menu is ContainerBlockMenu && menu.inventory === this@SimpleContainerBlockEntity
        }
    }

    override fun onOpen(player: PlayerEntity) {
        stateManager.openChest(player, world, pos, cachedState)
    }

    override fun onClose(player: PlayerEntity) {
        stateManager.closeChest(player, world, pos, cachedState)
    }

    fun onScheduledTick() {
        if (!removed) {
            stateManager.updateViewerCount(world, pos, cachedState)
        }
    }

    override fun writeScreenOpeningData(player: ServerPlayerEntity, buf: PacketByteBuf) {
        buf.writeBlockPos(pos)
    }
}
