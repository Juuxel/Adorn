package juuxel.adorn.block;

import juuxel.adorn.lib.AdornTags;
import juuxel.adorn.util.ShapeRotation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;

import java.util.HashSet;
import java.util.Set;

public class CopperPipeBlock extends Block implements SimpleWaterloggedBlock, BlockWithDescription {
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final String DESCRIPTION_KEY = "block.adorn.copper_pipe.description";
    private static final VoxelShape[] SHAPES = new VoxelShape[64];

    static {
        var center = box(6.0, 6.0, 6.0, 10.0, 10.0, 10.0);
        var pipes = ShapeRotation.buildShapeRotationsFromNorth(7, 7, 0, 9, 9, 8);
        pipes.put(Direction.UP, box(7.0, 8.0, 7.0, 9.0, 16.0, 9.0));
        pipes.put(Direction.DOWN, box(7.0, 0.0, 7.0, 9.0, 8.0, 9.0));
        var ringX = box(7.0, 6.0, 6.0, 9.0, 10.0, 10.0);
        var ringY = box(6.0, 7.0, 6.0, 10.0, 9.0, 10.0);
        var ringZ = box(6.0, 6.0, 7.0, 10.0, 10.0, 9.0);

        var booleans = new boolean[] { true, false };
        for (var north : booleans) {
            for (var east : booleans) {
                for (var south : booleans) {
                    for (var west : booleans) {
                        for (var up : booleans) {
                            for (var down : booleans) {
                                // If it spans multiple axes or only has one connection along an axis or if it has no connections
                                Set<Direction.Axis> axes = new HashSet<>();
                                if (west || east) {
                                    axes.add(Direction.Axis.X);
                                }
                                if (up || down) {
                                    axes.add(Direction.Axis.Y);
                                }
                                if (north || south) {
                                    axes.add(Direction.Axis.Z);
                                }

                                var hasCenter = (!north && !east && !south && !west && !up && !down) ||
                                    (north && !south) ||
                                    (east && !west) ||
                                    (south && !north) ||
                                    (west && !east) ||
                                    (up && !down) ||
                                    (down && !up) ||
                                    axes.size() > 1;

                                VoxelShape shape;
                                if (hasCenter) {
                                    shape = center;
                                } else if (east) { // Straight pipe along X axis
                                    shape = ringX;
                                } else if (up) { // Straight pipe along Y axis
                                    shape = ringY;
                                } else { // Straight pipe along Z axis
                                    shape = ringZ;
                                }

                                if (north) shape = Shapes.or(shape, pipes.get(Direction.NORTH));
                                if (east) shape = Shapes.or(shape, pipes.get(Direction.EAST));
                                if (south) shape = Shapes.or(shape, pipes.get(Direction.SOUTH));
                                if (west) shape = Shapes.or(shape, pipes.get(Direction.WEST));
                                if (up) shape = Shapes.or(shape, pipes.get(Direction.UP));
                                if (down) shape = Shapes.or(shape, pipes.get(Direction.DOWN));

                                SHAPES[getShapeKey(north, east, south, west, up, down)] = shape;
                            }
                        }
                    }
                }
            }
        }
    }

    public CopperPipeBlock(Properties settings) {
        super(settings);

        registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
    }

    @Override
    public String getDescriptionKey() {
        return DESCRIPTION_KEY;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPES[getShapeKey(state.getValue(NORTH), state.getValue(EAST), state.getValue(SOUTH), state.getValue(WEST), state.getValue(UP), state.getValue(DOWN))];
    }

    private static int getShapeKey(boolean north, boolean east, boolean south, boolean west, boolean up, boolean down) {
        int northB = north ? 1 : 0;
        int eastB = east ? 1 : 0;
        int southB = south ? 1 : 0;
        int westB = west ? 1 : 0;
        int upB = up ? 1 : 0;
        int downB = down ? 1 : 0;

        return northB << 5 | eastB << 4 | southB << 3 | westB << 2 | upB << 1 | downB;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        var state = defaultBlockState().setValue(WATERLOGGED, ctx.getLevel().getFluidState(ctx.getClickedPos()).getType() == Fluids.WATER);

        for (var direction : Direction.values()) {
            state = updateConnection(state, ctx.getLevel().getBlockState(ctx.getClickedPos().relative(direction)), direction);
        }

        return state;
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            tickView.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }

        return updateConnection(state, neighborState, direction);
    }

    private BlockState updateConnection(BlockState state, BlockState neighborState, Direction direction) {
        var property = switch (direction) {
            case DOWN -> DOWN;
            case UP -> UP;
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            case EAST -> EAST;
        };
        return state.setValue(property, shouldConnectTo(neighborState));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN, WATERLOGGED);
    }

    private static boolean shouldConnectTo(BlockState state) {
        return state.is(AdornTags.COPPER_PIPES_CONNECT_TO);
    }
}
