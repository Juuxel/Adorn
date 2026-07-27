package juuxel.adorn.block;

import com.google.common.base.Predicates;
import juuxel.adorn.criterion.AdornCriteria;
import juuxel.adorn.entity.AdornEntities;
import juuxel.adorn.entity.SeatEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

public abstract class SeatBlock extends Block {
    public static final BooleanProperty OCCUPIED = BlockStateProperties.OCCUPIED;

    public SeatBlock(Properties settings) {
        super(settings);

        if (isSittingEnabled()) {
            registerDefaultState(defaultBlockState().setValue(OCCUPIED, false));
        }
    }

    public abstract @Nullable Identifier getSittingStat();

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (!isSittingEnabled()) {
            return super.useWithoutItem(state, world, pos, player, hit);
        }

        var actualPos = getActualSeatPos(world, state, pos);
        var actualState = pos.equals(actualPos) ? state : world.getBlockState(actualPos);

        if (state != actualState && !(actualState.getBlock() instanceof SeatBlock)) {
            return InteractionResult.PASS;
        }

        var occupied = actualState.getValue(OCCUPIED);

        if (!occupied) {
            if (!world.isClientSide()) {
                var entity = new SeatEntity(AdornEntities.SEAT.get(), world);
                entity.setPos(actualPos);
                world.addFreshEntity(entity);
                world.setBlockAndUpdate(actualPos, actualState.setValue(OCCUPIED, true));
                player.startRiding(entity);

                var sittingStat = getSittingStat();
                if (sittingStat != null) {
                    player.awardStat(sittingStat);
                }

                if (player instanceof ServerPlayer serverPlayer) {
                    AdornCriteria.SIT_ON_BLOCK.get().trigger(serverPlayer, pos);
                }
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel world, BlockPos pos, boolean moved) {
        if (isSittingEnabled()) {
            var seats = world.getEntities(
                AdornEntities.SEAT.get(),
                new AABB(getActualSeatPos(world, state, pos)),
                Predicates.alwaysTrue()
            );
            for (var seat : seats) {
                seat.discard();
            }
        }
    }

    protected BlockPos getActualSeatPos(Level world, BlockState state, BlockPos pos) {
        return pos;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        if (isSittingEnabled()) builder.add(OCCUPIED);
    }

    protected boolean isSittingEnabled() {
        return true;
    }

    public double getSittingOffset(Level world, BlockState state, BlockPos pos) {
        return state.getCollisionShape(world, pos).max(Direction.Axis.Y);
    }

    public Direction getPreferredDismountDirection(BlockState state, Entity passenger) {
        return passenger.getDirection();
    }
}
