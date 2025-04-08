package juuxel.adorn.block;

import com.google.common.base.Predicates;
import juuxel.adorn.criterion.AdornCriteria;
import juuxel.adorn.entity.AdornEntities;
import juuxel.adorn.entity.SeatEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class SeatBlock extends Block {
    public static final BooleanProperty OCCUPIED = Properties.OCCUPIED;

    public SeatBlock(Settings settings) {
        super(settings);

        if (isSittingEnabled()) {
            setDefaultState(getDefaultState().with(OCCUPIED, false));
        }
    }

    public abstract @Nullable Identifier getSittingStat();

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!isSittingEnabled()) {
            return super.onUse(state, world, pos, player, hit);
        }

        var actualPos = getActualSeatPos(world, state, pos);
        var actualState = pos.equals(actualPos) ? state : world.getBlockState(actualPos);

        if (state != actualState && !(actualState.getBlock() instanceof SeatBlock)) {
            return ActionResult.PASS;
        }

        var occupied = actualState.get(OCCUPIED);

        if (!occupied) {
            if (!world.isClient) {
                var entity = new SeatEntity(AdornEntities.SEAT.get(), world);
                entity.setPos(actualPos);
                world.spawnEntity(entity);
                world.setBlockState(actualPos, actualState.with(OCCUPIED, true));
                player.startRiding(entity, true);

                var sittingStat = getSittingStat();
                if (sittingStat != null) {
                    player.incrementStat(sittingStat);
                }

                if (player instanceof ServerPlayerEntity serverPlayer) {
                    AdornCriteria.SIT_ON_BLOCK.get().trigger(serverPlayer, pos);
                }
            }
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    @Override
    protected void onStateReplaced(BlockState state, ServerWorld world, BlockPos pos, boolean moved) {
        if (isSittingEnabled()) {
            var seats = world.getEntitiesByType(
                AdornEntities.SEAT.get(),
                new Box(getActualSeatPos(world, state, pos)),
                Predicates.alwaysTrue()
            );
            for (var seat : seats) {
                seat.removeAllPassengers();
                seat.kill(world);
            }
        }
    }

    protected BlockPos getActualSeatPos(World world, BlockState state, BlockPos pos) {
        return pos;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        if (isSittingEnabled()) builder.add(OCCUPIED);
    }

    protected boolean isSittingEnabled() {
        return true;
    }

    public double getSittingOffset(World world, BlockState state, BlockPos pos) {
        return state.getCollisionShape(world, pos).getMax(Direction.Axis.Y);
    }

    public Direction getPreferredDismountDirection(BlockState state, Entity passenger) {
        return passenger.getHorizontalFacing();
    }
}
