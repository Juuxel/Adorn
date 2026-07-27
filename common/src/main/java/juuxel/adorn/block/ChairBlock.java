package juuxel.adorn.block;

import juuxel.adorn.lib.AdornStats;
import juuxel.adorn.util.ShapeRotation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

import java.util.Map;

public final class ChairBlock extends CarpetedBlock implements SimpleWaterloggedBlock, BlockWithDescription {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final String DESCRIPTION_KEY = "block.adorn.chair.description";
    private static final Map<Direction, VoxelShape> LOWER_SHAPES;
    private static final Map<Direction, VoxelShape> LOWER_SHAPES_WITH_CARPET;
    private static final Map<Direction, VoxelShape> UPPER_OUTLINE_SHAPES;

    static {
        var lowerSeatShape = Shapes.or(
            box(2.0, 8.0, 2.0, 14.0, 10.0, 14.0),
            // Legs
            box(2.0, 0.0, 2.0, 4.0, 8.0, 4.0),
            box(12.0, 0.0, 2.0, 14.0, 8.0, 4.0),
            box(2.0, 0.0, 12.0, 4.0, 8.0, 14.0),
            box(12.0, 0.0, 12.0, 14.0, 8.0, 14.0)
        );
        var lowerBackShapes = ShapeRotation.buildShapeRotations(2, 10, 2, 4, 24, 14);
        LOWER_SHAPES = ShapeRotation.mergeIntoShapeMap(lowerBackShapes, lowerSeatShape);
        LOWER_SHAPES_WITH_CARPET = ShapeRotation.mergeIntoShapeMap(LOWER_SHAPES, CARPET_SHAPE);

        var upperSeatShape = Shapes.or(
            box(2.0, -8.0, 2.0, 14.0, -6.0, 14.0),
            // Legs
            box(2.0, -16.0, 2.0, 4.0, -8.0, 4.0),
            box(12.0, -16.0, 2.0, 14.0, -8.0, 4.0),
            box(2.0, -16.0, 12.0, 4.0, -8.0, 14.0),
            box(12.0, -16.0, 12.0, 14.0, -8.0, 14.0)
        );
        var upperBackShapes = ShapeRotation.buildShapeRotations(2, -6, 2, 4, 8, 14);
        UPPER_OUTLINE_SHAPES = ShapeRotation.mergeIntoShapeMap(upperBackShapes, upperSeatShape);
    }

    public ChairBlock(Properties settings) {
        super(settings);

        registerDefaultState(defaultBlockState().setValue(HALF, DoubleBlockHalf.LOWER)
            .setValue(WATERLOGGED, false));
    }

    @Override
    public Identifier getSittingStat() {
        return AdornStats.SIT_ON_CHAIR;
    }

    @Override
    public String getDescriptionKey() {
        return DESCRIPTION_KEY;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, HALF, WATERLOGGED);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        var world = ctx.getLevel();
        var pos = ctx.getClickedPos();

        if (pos.getY() < world.getMaxY() && world.getBlockState(pos.above()).canBeReplaced(ctx)) {
            return super.getStateForPlacement(ctx).setValue(FACING, ctx.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, world.getFluidState(pos).getType() == Fluids.WATER);
        }

        return null;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            var downState = world.getBlockState(pos.below());
            return downState.getBlock() == this && downState.getValue(HALF) == DoubleBlockHalf.LOWER;
        }

        return super.canSurvive(state, world, pos);
    }

    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if (!world.isClientSide() && player.isCreative()) {
            DoublePlantBlock.preventDropFromBottomPart(world, pos, state, player);
        }

        return super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        world.setBlockAndUpdate(
            pos.above(),
            FluidUtil.updateFluidFromState(
                defaultBlockState()
                    .setValue(HALF, DoubleBlockHalf.UPPER)
                    .setValue(FACING, state.getValue(FACING)),
                world.getFluidState(pos.above())
            )
        );
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            if (isCarpetingEnabled() && state.getValue(CARPET).isPresent()) {
                return LOWER_SHAPES_WITH_CARPET.get(state.getValue(FACING));
            } else {
                return LOWER_SHAPES.get(state.getValue(FACING));
            }
        } else {
            return UPPER_OUTLINE_SHAPES.get(state.getValue(FACING));
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            if (isCarpetingEnabled() && state.getValue(CARPET).isPresent()) {
                return LOWER_SHAPES_WITH_CARPET.get(state.getValue(FACING));
            } else {
                return LOWER_SHAPES.get(state.getValue(FACING));
            }
        } else {
            return Shapes.empty(); // Let the bottom one handle the collision
        }
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            tickView.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }

        var half = state.getValue(HALF);

        // If updated from other half's direction vertically (LOWER + UP or UPPER + DOWN)
        if (direction.getAxis() == Direction.Axis.Y && (half == DoubleBlockHalf.LOWER) == (direction == Direction.UP)) {
            // If the other half is not a chair, break block
            if (neighborState.getBlock() != this) {
                return Blocks.AIR.defaultBlockState();
            } else {
                return state.setValue(FACING, neighborState.getValue(FACING));
            }
        } else {
            return super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random);
        }
    }

    @Override
    protected BlockPos getActualSeatPos(Level world, BlockState state, BlockPos pos) {
        return switch (state.getValue(HALF)) {
            case UPPER -> pos.below();
            case LOWER -> pos;
        };
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public boolean canStateBeCarpeted(BlockState state) {
        return super.canStateBeCarpeted(state) && state.getValue(HALF) == DoubleBlockHalf.LOWER;
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    public double getSittingOffset(Level world, BlockState state, BlockPos pos) {
        return 0.625; // 10/16
    }

    @Override
    public Direction getPreferredDismountDirection(BlockState state, Entity passenger) {
        return state.getValue(FACING);
    }
}
