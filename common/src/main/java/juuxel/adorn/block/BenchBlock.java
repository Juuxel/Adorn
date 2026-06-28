package juuxel.adorn.block;

import juuxel.adorn.lib.AdornStats;
import juuxel.adorn.util.AdornUtil;
import juuxel.adorn.util.Shapes;
import juuxel.adorn.util.verification.UsesBlockProperty;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

import java.util.ArrayList;
import java.util.List;

@UsesBlockProperty(Direction.Axis.class)
public final class BenchBlock extends SeatBlock implements Waterloggable, BlockWithDescription {
    public static final EnumProperty<Direction.Axis> AXIS = Properties.HORIZONTAL_AXIS;
    public static final BooleanProperty CONNECTED_N = BooleanProperty.of("connected_n");
    public static final BooleanProperty CONNECTED_P = BooleanProperty.of("connected_p");
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    private static final String DESCRIPTION_KEY = "block.adorn.bench.description";
    private static final VoxelShape X_TOP_SHAPE = createCuboidShape(0.0, 8.0, 1.0, 16.0, 10.0, 15.0);
    private static final VoxelShape Z_TOP_SHAPE = createCuboidShape(1.0, 8.0, 0.0, 15.0, 10.0, 16.0);
    private static final VoxelShape[] SHAPES = new VoxelShape[8];

    static {
        var legShapes = Shapes.buildShapeRotationsFromNorth(2, 0, 2, 14, 8, 4);
        var booleans = new boolean[] { true, false };

        for (var axis : new Direction.Axis[] { Direction.Axis.X, Direction.Axis.Z }) {
            var topShape = axis == Direction.Axis.X ? X_TOP_SHAPE : Z_TOP_SHAPE;
            var negativeLeg = legShapes.get(Direction.from(axis, Direction.AxisDirection.NEGATIVE));
            var positiveLeg = legShapes.get(Direction.from(axis, Direction.AxisDirection.POSITIVE));

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
                    SHAPES[key] = VoxelShapes.union(topShape, parts.toArray(VoxelShape[]::new));
                }
            }
        }
    }

    public BenchBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState()
            .with(AXIS, Direction.Axis.Z)
            .with(CONNECTED_N, false)
            .with(CONNECTED_P, false)
            .with(WATERLOGGED, false));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        var state = getDefaultState()
            .with(AXIS, AdornUtil.turnHorizontally(ctx.getHorizontalPlayerFacing().getAxis()))
            .with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
        return updateConnections(ctx.getWorld(), ctx.getBlockPos(), state);
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        if (state.get(WATERLOGGED)) {
            tickView.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return updateConnections(world, pos, state);
    }

    private BlockState updateConnections(BlockView world, BlockPos pos, BlockState state) {
        for (var axisDirection : Direction.AxisDirection.values()) {
            var property = axisDirection == Direction.AxisDirection.NEGATIVE ? CONNECTED_N : CONNECTED_P;
            var neighbor = world.getBlockState(pos.offset(state.get(AXIS), axisDirection.offset()));
            var connected = neighbor.getBlock() instanceof BenchBlock && neighbor.get(AXIS) == state.get(AXIS);
            state = state.with(property, connected);
        }
        return state;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES[getShapeKey(state.get(AXIS), state.get(CONNECTED_N), state.get(CONNECTED_P))];
    }

    private static int getShapeKey(Direction.Axis axis, boolean connectedN, boolean connectedP) {
        return (axis == Direction.Axis.X ? 1 : 0) << 2 | (connectedN ? 1 : 0) << 1 | (connectedP ? 1 : 0);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(AXIS, CONNECTED_N, CONNECTED_P, WATERLOGGED);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return switch (rotation) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                yield state.with(AXIS, AdornUtil.turnHorizontally(state.get(AXIS)));
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
        var axis = AdornUtil.turnHorizontally(state.get(AXIS));
        var passengerFacing = passenger.getHorizontalFacing();
        return passengerFacing.getAxis() == axis ? passengerFacing : Direction.get(Direction.AxisDirection.POSITIVE, axis);
    }
}
