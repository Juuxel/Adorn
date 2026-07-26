package juuxel.adorn.block;

import juuxel.adorn.lib.AdornStats;
import juuxel.adorn.util.AdornUtil;
import juuxel.adorn.util.ShapeRotation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;

import java.util.ArrayList;
import java.util.List;

public final class BenchBlock extends SeatBlock implements SimpleWaterloggedBlock, BlockWithDescription {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    public static final BooleanProperty CONNECTED_N = BooleanProperty.create("connected_n");
    public static final BooleanProperty CONNECTED_P = BooleanProperty.create("connected_p");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final String DESCRIPTION_KEY = "block.adorn.bench.description";
    private static final VoxelShape X_TOP_SHAPE = box(0.0, 8.0, 1.0, 16.0, 10.0, 15.0);
    private static final VoxelShape Z_TOP_SHAPE = box(1.0, 8.0, 0.0, 15.0, 10.0, 16.0);
    private static final VoxelShape[] SHAPES = new VoxelShape[8];

    static {
        var legShapes = ShapeRotation.buildShapeRotationsFromNorth(2, 0, 2, 14, 8, 4);
        var booleans = new boolean[] { true, false };

        for (var axis : new Direction.Axis[] { Direction.Axis.X, Direction.Axis.Z }) {
            var topShape = axis == Direction.Axis.X ? X_TOP_SHAPE : Z_TOP_SHAPE;
            var negativeLeg = legShapes.get(Direction.fromAxisAndDirection(axis, Direction.AxisDirection.NEGATIVE));
            var positiveLeg = legShapes.get(Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE));

            for (var connectedN : booleans) {
                for (var connectedP : booleans) {
                    List<VoxelShape> parts = new ArrayList<>();

                    if (!connectedN) {
                        parts.add(negativeLeg);
                    }

                    if (!connectedP) {
                        parts.add(positiveLeg);
                    }

                    int key = getShapeKey(axis, connectedN, connectedP);
                    SHAPES[key] = Shapes.or(topShape, parts.toArray(VoxelShape[]::new));
                }
            }
        }
    }

    public BenchBlock(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState()
            .setValue(AXIS, Direction.Axis.Z)
            .setValue(CONNECTED_N, false)
            .setValue(CONNECTED_P, false)
            .setValue(WATERLOGGED, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        var state = defaultBlockState()
            .setValue(AXIS, AdornUtil.turnHorizontally(ctx.getHorizontalDirection().getAxis()))
            .setValue(WATERLOGGED, ctx.getLevel().getFluidState(ctx.getClickedPos()).getType() == Fluids.WATER);
        return updateConnections(ctx.getLevel(), ctx.getClickedPos(), state);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            tickView.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }

        return updateConnections(world, pos, state);
    }

    private BlockState updateConnections(BlockGetter world, BlockPos pos, BlockState state) {
        for (var axisDirection : Direction.AxisDirection.values()) {
            var property = axisDirection == Direction.AxisDirection.NEGATIVE ? CONNECTED_N : CONNECTED_P;
            var neighbor = world.getBlockState(pos.relative(state.getValue(AXIS), axisDirection.getStep()));
            var connected = neighbor.getBlock() instanceof BenchBlock && neighbor.getValue(AXIS) == state.getValue(AXIS);
            state = state.setValue(property, connected);
        }
        return state;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPES[getShapeKey(state.getValue(AXIS), state.getValue(CONNECTED_N), state.getValue(CONNECTED_P))];
    }

    private static int getShapeKey(Direction.Axis axis, boolean connectedN, boolean connectedP) {
        return (axis == Direction.Axis.X ? 1 : 0) << 2 | (connectedN ? 1 : 0) << 1 | (connectedP ? 1 : 0);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(AXIS, CONNECTED_N, CONNECTED_P, WATERLOGGED);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return switch (rotation) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                yield state.setValue(AXIS, AdornUtil.turnHorizontally(state.getValue(AXIS)));
            default:
                yield state;
        };
    }

    @Override
    public Identifier getSittingStat() {
        return AdornStats.SIT_ON_BENCH;
    }

    @Override
    public String getDescriptionKey() {
        return DESCRIPTION_KEY;
    }

    @Override
    public Direction getPreferredDismountDirection(BlockState state, Entity passenger) {
        var axis = AdornUtil.turnHorizontally(state.getValue(AXIS));
        var passengerFacing = passenger.getDirection();
        return passengerFacing.getAxis() == axis ? passengerFacing : Direction.get(Direction.AxisDirection.POSITIVE, axis);
    }
}
