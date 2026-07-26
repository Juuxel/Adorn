package juuxel.adorn.block.entity;

import juuxel.adorn.menu.ContainerBlockMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.entity.ContainerUser;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * A simple container block entity with a menu.
 * These block entities also send game events when they are opened/closed.
 */
public abstract class SimpleContainerBlockEntity extends BaseContainerBlockEntity {
    private final ContainerOpenersCounter viewerCountManager = new ContainerOpenersCounter() {
        @Override
        protected void onOpen(Level world, BlockPos pos, BlockState state) {
        }

        @Override
        protected void onClose(Level world, BlockPos pos, BlockState state) {
        }

        @Override
        protected void openerCountChanged(Level world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
        }

        @Override
        public boolean isOwnContainer(Player player) {
            return player.containerMenu instanceof ContainerBlockMenu cbm && cbm.getInventory() == SimpleContainerBlockEntity.this;
        }
    };

    public SimpleContainerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int size) {
        super(type, pos, state, size);
    }

    @Override
    public void startOpen(ContainerUser user) {
        if (!remove && !user.getLivingEntity().isSpectator()) {
            viewerCountManager.incrementOpeners(user.getLivingEntity(), level, worldPosition, getBlockState(), user.getContainerInteractionRange());
        }
    }

    @Override
    public void stopOpen(ContainerUser user) {
        if (!remove && !user.getLivingEntity().isSpectator()) {
            viewerCountManager.decrementOpeners(user.getLivingEntity(), level, worldPosition, getBlockState());
        }
    }

    public void onScheduledTick() {
        if (!remove) {
            viewerCountManager.recheckOpeners(level, worldPosition, getBlockState());
        }
    }
}
