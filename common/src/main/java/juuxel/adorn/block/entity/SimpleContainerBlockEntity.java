package juuxel.adorn.block.entity;

import juuxel.adorn.menu.ContainerBlockMenu;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ViewerCountManager;
import net.minecraft.entity.ContainerUser;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * A simple container block entity with a menu.
 * These block entities also send game events when they are opened/closed.
 */
public abstract class SimpleContainerBlockEntity extends BaseContainerBlockEntity {
    private final ViewerCountManager viewerCountManager = new ViewerCountManager() {
        @Override
        protected void onContainerOpen(World world, BlockPos pos, BlockState state) {
        }

        @Override
        protected void onContainerClose(World world, BlockPos pos, BlockState state) {
        }

        @Override
        protected void onViewerCountUpdate(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
        }

        @Override
        public boolean isPlayerViewing(PlayerEntity player) {
            return player.menu instanceof ContainerBlockMenu cbm && cbm.getInventory() == SimpleContainerBlockEntity.this;
        }
    };

    public SimpleContainerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int size) {
        super(type, pos, state, size);
    }

    @Override
    public void onOpen(ContainerUser user) {
        if (!removed && !user.asLivingEntity().isSpectator()) {
            viewerCountManager.openContainer(user.asLivingEntity(), world, pos, getCachedState(), user.getContainerInteractionRange());
        }
    }

    @Override
    public void onClose(ContainerUser user) {
        if (!removed && !user.asLivingEntity().isSpectator()) {
            viewerCountManager.closeContainer(user.asLivingEntity(), world, pos, getCachedState());
        }
    }

    public void onScheduledTick() {
        if (!removed) {
            viewerCountManager.updateViewerCount(world, pos, getCachedState());
        }
    }
}
