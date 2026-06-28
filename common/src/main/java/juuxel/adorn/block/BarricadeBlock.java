package juuxel.adorn.block;

import juuxel.adorn.util.Shapes;
import juuxel.adorn.util.verification.UsesBlockProperty;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

import java.util.Map;

@UsesBlockProperty(Direction.class)
public final class BarricadeBlock extends Block implements BlockWithDescription {
    public static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    private static final Map<Direction, VoxelShape> OUTLINE_SHAPES = Shapes.mergeShapeMaps(
        // planks
        Shapes.buildShapeRotationsFromNorth(0, 6, 6, 16, 16, 9),
        // legs upper
        Shapes.buildShapeRotationsFromNorth(2, 2, 7, 14, 6, 9),
        // legs lower
        Shapes.buildShapeRotationsFromNorth(2, 0, 4, 14, 2, 12)
    );
    private static final Map<Direction, VoxelShape> COLLISION_SHAPES = Shapes.mergeShapeMaps(
        // planks
        Shapes.buildShapeRotationsFromNorth(0, 6, 6, 16, 24, 9),
        // legs upper
        Shapes.buildShapeRotationsFromNorth(2, 2, 7, 14, 6, 9),
        // legs lower
        Shapes.buildShapeRotationsFromNorth(2, 0, 4, 14, 2, 12)
    );

    public BarricadeBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(WATERLOGGED, false));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState()
            .with(FACING, ctx.getHorizontalPlayerFacing().getOpposite())
            .with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        if (state.get(WATERLOGGED)) {
            tickView.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return state;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return OUTLINE_SHAPES.get(state.get(FACING));
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return COLLISION_SHAPES.get(state.get(FACING));
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING, WATERLOGGED);
    }
}
