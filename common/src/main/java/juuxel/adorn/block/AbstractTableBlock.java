package juuxel.adorn.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class AbstractTableBlock extends CarpetedBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public AbstractTableBlock(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
    }

    protected abstract boolean canConnectTo(BlockState state, Direction sideOfSelf);

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(NORTH, EAST, SOUTH, WEST, WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return updateConnections(
            super.getStateForPlacement(ctx)
                .setValue(WATERLOGGED, ctx.getLevel().getFluidState(ctx.getClickedPos()).getType() == Fluids.WATER),
            ctx.getLevel(),
            ctx.getClickedPos()
        );
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            tickView.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }

        return updateConnections(super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random), world, pos);
    }

    private BlockState updateConnections(BlockState state, BlockGetter world, BlockPos pos) {
        return state.setValue(NORTH, canConnectTo(world.getBlockState(pos.relative(Direction.NORTH)), Direction.NORTH))
            .setValue(EAST, canConnectTo(world.getBlockState(pos.relative(Direction.EAST)), Direction.EAST))
            .setValue(SOUTH, canConnectTo(world.getBlockState(pos.relative(Direction.SOUTH)), Direction.SOUTH))
            .setValue(WEST, canConnectTo(world.getBlockState(pos.relative(Direction.WEST)), Direction.WEST));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return getShapeForKey(
            getShapeKey(
                state.getValue(NORTH), state.getValue(EAST), state.getValue(SOUTH), state.getValue(WEST),
                isCarpetingEnabled() && state.getValue(CARPET).isPresent()
            )
        );
    }

    protected static int getShapeKey(boolean north, boolean east, boolean south, boolean west, boolean hasCarpet) {
        return (north ? 1 : 0) << 4 | (east ? 1 : 0) << 3 | (south ? 1 : 0) << 2 | (west ? 1 : 0) << 1 | (hasCarpet ? 1 : 0);
    }

    protected abstract VoxelShape getShapeForKey(int key);

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }
}
