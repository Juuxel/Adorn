package juuxel.adorn.block;

import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.lib.AdornStats;
import juuxel.adorn.util.Shapes;
import juuxel.adorn.util.verification.UsesBlockProperty;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@UsesBlockProperty({Direction.class, DoubleBlockHalf.class})
public final class ChairBlock extends CarpetedBlock implements Waterloggable, BlockWithDescription {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    private static final String DESCRIPTION_KEY = "block.adorn.chair.description";
    private static final Map<Direction, VoxelShape> LOWER_SHAPES;
    private static final Map<Direction, VoxelShape> LOWER_SHAPES_WITH_CARPET;
    private static final Map<Direction, VoxelShape> UPPER_OUTLINE_SHAPES;

    static {
        var lowerSeatShape = VoxelShapes.union(
            createCuboidShape(2.0, 8.0, 2.0, 14.0, 10.0, 14.0),
            // Legs
            createCuboidShape(2.0, 0.0, 2.0, 4.0, 8.0, 4.0),
            createCuboidShape(12.0, 0.0, 2.0, 14.0, 8.0, 4.0),
            createCuboidShape(2.0, 0.0, 12.0, 4.0, 8.0, 14.0),
            createCuboidShape(12.0, 0.0, 12.0, 14.0, 8.0, 14.0)
        );
        var lowerBackShapes = Shapes.buildShapeRotations(2, 10, 2, 4, 24, 14);
        LOWER_SHAPES = Shapes.mergeIntoShapeMap(lowerBackShapes, lowerSeatShape);
        LOWER_SHAPES_WITH_CARPET = Shapes.mergeIntoShapeMap(LOWER_SHAPES, CARPET_SHAPE);

        var upperSeatShape = VoxelShapes.union(
            createCuboidShape(2.0, -8.0, 2.0, 14.0, -6.0, 14.0),
            // Legs
            createCuboidShape(2.0, -16.0, 2.0, 4.0, -8.0, 4.0),
            createCuboidShape(12.0, -16.0, 2.0, 14.0, -8.0, 4.0),
            createCuboidShape(2.0, -16.0, 12.0, 4.0, -8.0, 14.0),
            createCuboidShape(12.0, -16.0, 12.0, 14.0, -8.0, 14.0)
        );
        var upperBackShapes = Shapes.buildShapeRotations(2, -6, 2, 4, 8, 14);
        UPPER_OUTLINE_SHAPES = Shapes.mergeIntoShapeMap(upperBackShapes, upperSeatShape);
    }

    public ChairBlock(BlockVariant variant) {
        super(variant.createSettings());

        setDefaultState(getDefaultState().with(HALF, DoubleBlockHalf.LOWER)
            .with(WATERLOGGED, false));
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
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING, HALF, WATERLOGGED);
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        var world = ctx.getWorld();
        var pos = ctx.getBlockPos();

        if (pos.getY() < world.getTopY() - 1 && world.getBlockState(pos.up()).canReplace(ctx)) {
            return super.getPlacementState(ctx).with(FACING, ctx.getHorizontalPlayerFacing().getOpposite())
                .with(WATERLOGGED, world.getFluidState(pos).getFluid() == Fluids.WATER);
        }

        return null;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (state.get(HALF) == DoubleBlockHalf.UPPER) {
            var downState = world.getBlockState(pos.down());
            return downState.getBlock() == this && downState.get(HALF) == DoubleBlockHalf.LOWER;
        }

        return super.canPlaceAt(state, world, pos);
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && player.isCreative()) {
            TallPlantBlock.onBreakInCreative(world, pos, state, player);
        }

        return super.onBreak(world, pos, state, player);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        world.setBlockState(
            pos.up(),
            FluidUtil.updateFluidFromState(
                getDefaultState()
                    .with(HALF, DoubleBlockHalf.UPPER)
                    .with(FACING, state.get(FACING)),
                world.getFluidState(pos.up())
            )
        );
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(HALF) == DoubleBlockHalf.LOWER) {
            if (isCarpetingEnabled() && state.get(CARPET).isPresent()) {
                return LOWER_SHAPES_WITH_CARPET.get(state.get(FACING));
            } else {
                return LOWER_SHAPES.get(state.get(FACING));
            }
        } else {
            return UPPER_OUTLINE_SHAPES.get(state.get(FACING));
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(HALF) == DoubleBlockHalf.LOWER) {
            if (isCarpetingEnabled() && state.get(CARPET).isPresent()) {
                return LOWER_SHAPES_WITH_CARPET.get(state.get(FACING));
            } else {
                return LOWER_SHAPES.get(state.get(FACING));
            }
        } else {
            return VoxelShapes.empty(); // Let the bottom one handle the collision
        }
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        var half = state.get(HALF);

        // If updated from other half's direction vertically (LOWER + UP or UPPER + DOWN)
        if (direction.getAxis() == Direction.Axis.Y && (half == DoubleBlockHalf.LOWER) == (direction == Direction.UP)) {
            // If the other half is not a chair, break block
            if (neighborState.getBlock() != this) {
                return Blocks.AIR.getDefaultState();
            } else {
                return state.with(FACING, neighborState.get(FACING));
            }
        } else {
            return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        }
    }

    @Override
    protected BlockPos getActualSeatPos(World world, BlockState state, BlockPos pos) {
        return switch (state.get(HALF)) {
            case UPPER -> pos.down();
            case LOWER -> pos;
        };
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public boolean canStateBeCarpeted(BlockState state) {
        return super.canStateBeCarpeted(state) && state.get(HALF) == DoubleBlockHalf.LOWER;
    }

    @Override
    public boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }

    @Override
    public double getSittingOffset(World world, BlockState state, BlockPos pos) {
        return 0.625; // 10/16
    }

    @Override
    public Direction getPreferredDismountDirection(BlockState state, Entity passenger) {
        return state.get(FACING);
    }
}
