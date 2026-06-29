package juuxel.adorn.block;

import juuxel.adorn.util.Shapes;
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
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

import java.util.Map;

public final class PicketFenceBlock extends Block implements Waterloggable, BlockWithDescription {
    public static final EnumProperty<Shape> SHAPE = EnumProperty.of("shape", Shape.class);
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    private static final Map<Direction, VoxelShape> STRAIGHT_OUTLINE_SHAPES = Shapes.buildShapeRotationsFromNorth(0, 0, 7, 16, 16, 9);
    private static final Map<Direction, VoxelShape> CORNER_OUTLINE_SHAPES = Shapes.mergeIntoShapeMap(
        Shapes.mergeShapeMaps(
            Shapes.buildShapeRotationsFromNorth(0, 0, 7, 9, 16, 9),
            Shapes.buildShapeRotationsFromNorth(7, 0, 9, 9, 16, 16)
        ),
        PostBlock.Y_SHAPE
    );
    private static final Map<Direction, VoxelShape> STRAIGHT_COLLISION_SHAPES = Shapes.buildShapeRotationsFromNorth(0, 0, 7, 16, 24, 9);
    private static final Map<Direction, VoxelShape> CORNER_COLLISION_SHAPES = Shapes.mergeIntoShapeMap(
        Shapes.mergeShapeMaps(
            Shapes.buildShapeRotationsFromNorth(0, 0, 7, 9, 24, 9),
            Shapes.buildShapeRotationsFromNorth(7, 0, 9, 9, 24, 16)
        ),
        createCuboidShape(6.0, 0.0, 6.0, 10.0, 24.0, 10.0)
    );
    
    public PicketFenceBlock(Settings settings) {
        super(settings);

        setDefaultState(getDefaultState().with(WATERLOGGED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(SHAPE, FACING, WATERLOGGED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        var state = getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite())
            .with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
        return updateShape(ctx.getWorld(), ctx.getBlockPos(), state);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction.getAxis() == state.get(FACING).getAxis()) {
            return updateShape(world, pos, state);
        }

        return state;
    }

    private BlockState updateShape(WorldAccess world, BlockPos pos, BlockState state) {
        var fenceFacing = state.get(FACING);
        for (var side : new Direction[] { fenceFacing.getOpposite(), fenceFacing }) {
            var inner = side == fenceFacing;
            var neighborState = world.getBlockState(pos.offset(side));
            var neighborBlock = neighborState.getBlock();
            var neighborFacing = neighborBlock instanceof PicketFenceBlock ? neighborState.get(FACING) : null;

            Shape shape;
            if (neighborFacing == fenceFacing.rotateYClockwise()) {
                shape = inner ? Shape.CLOCKWISE_INNER_CORNER : Shape.CLOCKWISE_CORNER;
            } else if (neighborFacing == fenceFacing.rotateYCounterclockwise()) {
                shape = inner ? Shape.COUNTERCLOCKWISE_INNER_CORNER : Shape.COUNTERCLOCKWISE_CORNER;
            } else {
                shape = Shape.STRAIGHT;
            }

            // Prevent funny connections
            if (!(neighborBlock instanceof PicketFenceBlock picketFence) || !picketFence.connectsTo(neighborState, side.getOpposite())) {
                shape = Shape.STRAIGHT;
            }

            if (shape != Shape.STRAIGHT) {
                return state.with(SHAPE, shape);
            }
        }

        return state.with(SHAPE, Shape.STRAIGHT);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(SHAPE)) {
            case STRAIGHT -> STRAIGHT_OUTLINE_SHAPES.get(state.get(FACING));
            case CLOCKWISE_CORNER -> CORNER_OUTLINE_SHAPES.get(state.get(FACING));
            case COUNTERCLOCKWISE_CORNER -> CORNER_OUTLINE_SHAPES.get(state.get(FACING).rotateYCounterclockwise());
            case CLOCKWISE_INNER_CORNER -> CORNER_OUTLINE_SHAPES.get(state.get(FACING).getOpposite());
            case COUNTERCLOCKWISE_INNER_CORNER -> CORNER_OUTLINE_SHAPES.get(state.get(FACING).rotateYClockwise());
        };
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(SHAPE)) {
            case STRAIGHT -> STRAIGHT_COLLISION_SHAPES.get(state.get(FACING));
            case CLOCKWISE_CORNER -> CORNER_COLLISION_SHAPES.get(state.get(FACING));
            case COUNTERCLOCKWISE_CORNER -> CORNER_COLLISION_SHAPES.get(state.get(FACING).rotateYCounterclockwise());
            case CLOCKWISE_INNER_CORNER -> CORNER_COLLISION_SHAPES.get(state.get(FACING).getOpposite());
            case COUNTERCLOCKWISE_INNER_CORNER -> CORNER_COLLISION_SHAPES.get(state.get(FACING).rotateYClockwise());
        };
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.with(FACING, mirror.apply(state.get(FACING)));
    }

    @Override
    public boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }

    public boolean sideCoversSmallSquare(BlockState state) {
        return state.get(SHAPE) != Shape.STRAIGHT;
    }

    private boolean connectsTo(BlockState state, Direction direction) {
        if (!direction.getAxis().isHorizontal()) return false;

        var facing = state.get(FACING);
        return switch (state.get(SHAPE)) {
            case STRAIGHT -> facing.getAxis() != direction.getAxis();
            case CLOCKWISE_CORNER -> direction == facing.rotateYCounterclockwise() || direction == facing.getOpposite();
            case COUNTERCLOCKWISE_CORNER -> direction == facing.rotateYClockwise() || direction == facing.getOpposite();
            case CLOCKWISE_INNER_CORNER -> direction == facing.rotateYClockwise() || direction == facing;
            case COUNTERCLOCKWISE_INNER_CORNER -> direction == facing.rotateYCounterclockwise() || direction == facing;
        };
    }

    public enum Shape implements StringIdentifiable {
        STRAIGHT("straight"),
        CLOCKWISE_CORNER("clockwise_corner"),
        COUNTERCLOCKWISE_CORNER("counterclockwise_corner"),
        CLOCKWISE_INNER_CORNER("clockwise_inner_corner"),
        COUNTERCLOCKWISE_INNER_CORNER("counterclockwise_inner_corner");

        private final String id;

        Shape(String id) {
            this.id = id;
        }

        @Override
        public String asString() {
            return id;
        }
    }
}
