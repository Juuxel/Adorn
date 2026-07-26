package juuxel.adorn.block;

import juuxel.adorn.util.ShapeRotation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;

import java.util.Map;

public final class PicketFenceBlock extends Block implements SimpleWaterloggedBlock, BlockWithDescription {
    public static final EnumProperty<Shape> SHAPE = EnumProperty.create("shape", Shape.class);
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final Map<Direction, VoxelShape> STRAIGHT_OUTLINE_SHAPES = ShapeRotation.buildShapeRotationsFromNorth(0, 0, 7, 16, 16, 9);
    private static final Map<Direction, VoxelShape> CORNER_OUTLINE_SHAPES = ShapeRotation.mergeIntoShapeMap(
        ShapeRotation.mergeShapeMaps(
            ShapeRotation.buildShapeRotationsFromNorth(0, 0, 7, 9, 16, 9),
            ShapeRotation.buildShapeRotationsFromNorth(7, 0, 9, 9, 16, 16)
        ),
        PostBlock.Y_SHAPE
    );
    private static final Map<Direction, VoxelShape> STRAIGHT_COLLISION_SHAPES = ShapeRotation.buildShapeRotationsFromNorth(0, 0, 7, 16, 24, 9);
    private static final Map<Direction, VoxelShape> CORNER_COLLISION_SHAPES = ShapeRotation.mergeIntoShapeMap(
        ShapeRotation.mergeShapeMaps(
            ShapeRotation.buildShapeRotationsFromNorth(0, 0, 7, 9, 24, 9),
            ShapeRotation.buildShapeRotationsFromNorth(7, 0, 9, 9, 24, 16)
        ),
        box(6.0, 0.0, 6.0, 10.0, 24.0, 10.0)
    );
    
    public PicketFenceBlock(Properties settings) {
        super(settings);

        registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(SHAPE, FACING, WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        var state = defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite())
            .setValue(WATERLOGGED, ctx.getLevel().getFluidState(ctx.getClickedPos()).getType() == Fluids.WATER);
        return updateShape(ctx.getLevel(), ctx.getClickedPos(), state);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (direction.getAxis() == state.getValue(FACING).getAxis()) {
            return updateShape(world, pos, state);
        }

        return state;
    }

    private BlockState updateShape(BlockGetter world, BlockPos pos, BlockState state) {
        var fenceFacing = state.getValue(FACING);
        for (var side : new Direction[] { fenceFacing.getOpposite(), fenceFacing }) {
            var inner = side == fenceFacing;
            var neighborState = world.getBlockState(pos.relative(side));
            var neighborBlock = neighborState.getBlock();
            var neighborFacing = neighborBlock instanceof PicketFenceBlock ? neighborState.getValue(FACING) : null;

            Shape shape;
            if (neighborFacing == fenceFacing.getClockWise()) {
                shape = inner ? Shape.CLOCKWISE_INNER_CORNER : Shape.CLOCKWISE_CORNER;
            } else if (neighborFacing == fenceFacing.getCounterClockWise()) {
                shape = inner ? Shape.COUNTERCLOCKWISE_INNER_CORNER : Shape.COUNTERCLOCKWISE_CORNER;
            } else {
                shape = Shape.STRAIGHT;
            }

            // Prevent funny connections
            if (!(neighborBlock instanceof PicketFenceBlock picketFence) || !picketFence.connectsTo(neighborState, side.getOpposite())) {
                shape = Shape.STRAIGHT;
            }

            if (shape != Shape.STRAIGHT) {
                return state.setValue(SHAPE, shape);
            }
        }

        return state.setValue(SHAPE, Shape.STRAIGHT);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(SHAPE)) {
            case STRAIGHT -> STRAIGHT_OUTLINE_SHAPES.get(state.getValue(FACING));
            case CLOCKWISE_CORNER -> CORNER_OUTLINE_SHAPES.get(state.getValue(FACING));
            case COUNTERCLOCKWISE_CORNER -> CORNER_OUTLINE_SHAPES.get(state.getValue(FACING).getCounterClockWise());
            case CLOCKWISE_INNER_CORNER -> CORNER_OUTLINE_SHAPES.get(state.getValue(FACING).getOpposite());
            case COUNTERCLOCKWISE_INNER_CORNER -> CORNER_OUTLINE_SHAPES.get(state.getValue(FACING).getClockWise());
        };
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(SHAPE)) {
            case STRAIGHT -> STRAIGHT_COLLISION_SHAPES.get(state.getValue(FACING));
            case CLOCKWISE_CORNER -> CORNER_COLLISION_SHAPES.get(state.getValue(FACING));
            case COUNTERCLOCKWISE_CORNER -> CORNER_COLLISION_SHAPES.get(state.getValue(FACING).getCounterClockWise());
            case CLOCKWISE_INNER_CORNER -> CORNER_COLLISION_SHAPES.get(state.getValue(FACING).getOpposite());
            case COUNTERCLOCKWISE_INNER_CORNER -> CORNER_COLLISION_SHAPES.get(state.getValue(FACING).getClockWise());
        };
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    public boolean sideCoversSmallSquare(BlockState state) {
        return state.getValue(SHAPE) != Shape.STRAIGHT;
    }

    private boolean connectsTo(BlockState state, Direction direction) {
        if (!direction.getAxis().isHorizontal()) return false;

        var facing = state.getValue(FACING);
        return switch (state.getValue(SHAPE)) {
            case STRAIGHT -> facing.getAxis() != direction.getAxis();
            case CLOCKWISE_CORNER -> direction == facing.getCounterClockWise() || direction == facing.getOpposite();
            case COUNTERCLOCKWISE_CORNER -> direction == facing.getClockWise() || direction == facing.getOpposite();
            case CLOCKWISE_INNER_CORNER -> direction == facing.getClockWise() || direction == facing;
            case COUNTERCLOCKWISE_INNER_CORNER -> direction == facing.getCounterClockWise() || direction == facing;
        };
    }

    public enum Shape implements StringRepresentable {
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
        public String getSerializedName() {
            return id;
        }
    }
}
