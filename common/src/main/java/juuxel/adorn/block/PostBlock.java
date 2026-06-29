package juuxel.adorn.block;

import juuxel.adorn.block.variant.BlockVariant;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

public final class PostBlock extends Block implements BlockWithDescription, Waterloggable {
    public static final EnumProperty<Direction.Axis> AXIS = Properties.AXIS;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    public static final VoxelShape X_SHAPE = createCuboidShape(0.0, 6.0, 6.0, 16.0, 10.0, 10.0);
    public static final VoxelShape Y_SHAPE = createCuboidShape(6.0, 0.0, 6.0, 10.0, 16.0, 10.0);
    public static final VoxelShape Z_SHAPE = createCuboidShape(6.0, 6.0, 0.0, 10.0, 10.0, 16.0);

    private static final String DESCRIPTION_KEY = "block.adorn.post.description";

    public PostBlock(BlockVariant variant) {
        super(variant.createSettings());
        setDefaultState(getDefaultState().with(AXIS, Direction.Axis.Y).with(WATERLOGGED, false));
    }

    @Override
    public String getDescriptionKey() {
        return DESCRIPTION_KEY;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(AXIS, WATERLOGGED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(AXIS, ctx.getSide().getAxis())
            .with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(AXIS)) {
            case X -> X_SHAPE;
            case Y -> Y_SHAPE;
            case Z -> Z_SHAPE;
        };
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        if (rotation == BlockRotation.COUNTERCLOCKWISE_90 || rotation == BlockRotation.CLOCKWISE_90) {
            return switch (state.get(AXIS)) {
                case X -> state.with(AXIS, Direction.Axis.Z);
                case Z -> state.with(AXIS, Direction.Axis.X);
                default -> state;
            };
        }

        return state;
    }

    @Override
    public boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }
}
