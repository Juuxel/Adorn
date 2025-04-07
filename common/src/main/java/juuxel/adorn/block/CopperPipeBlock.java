package juuxel.adorn.block;

import juuxel.adorn.lib.AdornTags;
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
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

import java.util.HashSet;
import java.util.Set;

public class CopperPipeBlock extends Block implements Waterloggable, BlockWithDescription {
    public static final BooleanProperty NORTH = Properties.NORTH;
    public static final BooleanProperty EAST = Properties.EAST;
    public static final BooleanProperty SOUTH = Properties.SOUTH;
    public static final BooleanProperty WEST = Properties.WEST;
    public static final BooleanProperty UP = Properties.UP;
    public static final BooleanProperty DOWN = Properties.DOWN;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    private static final String DESCRIPTION_KEY = "block.adorn.copper_pipe.description";
    private static final VoxelShape[] SHAPES = new VoxelShape[64];

    static {
        var center = createCuboidShape(6.0, 6.0, 6.0, 10.0, 10.0, 10.0);
        var pipes = Shapes.buildShapeRotationsFromNorth(7, 7, 0, 9, 9, 8);
        pipes.put(Direction.UP, createCuboidShape(7.0, 8.0, 7.0, 9.0, 16.0, 9.0));
        pipes.put(Direction.DOWN, createCuboidShape(7.0, 0.0, 7.0, 9.0, 8.0, 9.0));
        var ringX = createCuboidShape(7.0, 6.0, 6.0, 9.0, 10.0, 10.0);
        var ringY = createCuboidShape(6.0, 7.0, 6.0, 10.0, 9.0, 10.0);
        var ringZ = createCuboidShape(6.0, 6.0, 7.0, 10.0, 10.0, 9.0);

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

                                if (north) shape = VoxelShapes.union(shape, pipes.get(Direction.NORTH));
                                if (east) shape = VoxelShapes.union(shape, pipes.get(Direction.EAST));
                                if (south) shape = VoxelShapes.union(shape, pipes.get(Direction.SOUTH));
                                if (west) shape = VoxelShapes.union(shape, pipes.get(Direction.WEST));
                                if (up) shape = VoxelShapes.union(shape, pipes.get(Direction.UP));
                                if (down) shape = VoxelShapes.union(shape, pipes.get(Direction.DOWN));

                                SHAPES[getShapeKey(north, east, south, west, up, down)] = shape;
                            }
                        }
                    }
                }
            }
        }
    }

    public CopperPipeBlock(Settings settings) {
        super(settings);

        setDefaultState(getDefaultState().with(WATERLOGGED, false));
    }

    @Override
    public String getDescriptionKey() {
        return DESCRIPTION_KEY;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES[getShapeKey(state.get(NORTH), state.get(EAST), state.get(SOUTH), state.get(WEST), state.get(UP), state.get(DOWN))];
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
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        var state = getDefaultState().with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);

        for (var direction : Direction.values()) {
            state = updateConnection(state, ctx.getWorld().getBlockState(ctx.getBlockPos().offset(direction)), direction);
        }

        return state;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
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
        return state.with(property, shouldConnectTo(neighborState));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN, WATERLOGGED);
    }

    private static boolean shouldConnectTo(BlockState state) {
        return state.isIn(AdornTags.COPPER_PIPES_CONNECT_TO);
    }
}
