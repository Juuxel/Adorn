package juuxel.adorn.block;

import com.mojang.serialization.MapCodec;
import juuxel.adorn.lib.AdornStats;
import juuxel.adorn.platform.PlatformBridges;
import juuxel.adorn.util.ShapeRotation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public final class ShelfBlock extends VisibleBlockWithEntity implements SimpleWaterloggedBlock, BlockWithDescription {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final String DESCRIPTION_KEY = "block.adorn.shelf.description";
    private static final Map<Direction, VoxelShape> SHAPES = ShapeRotation.buildShapeRotations(0, 5, 0, 7, 6, 16);

    public ShelfBlock(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
    }

    @Override
    public String getDescriptionKey() {
        return DESCRIPTION_KEY;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }

    // Based on WallTorchBlock.canPlaceAt
    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        var facing = state.getValue(FACING);
        var neighborPos = pos.relative(facing.getOpposite());
        return world.getBlockState(neighborPos).isFaceSturdy(world, neighborPos, facing);
    }

    // Based on WallTorchBlock.getPlacementState
    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        var waterlogged = ctx.getLevel().getFluidState(ctx.getClickedPos()).getType() == Fluids.WATER;
        for (var direction : ctx.getNearestLookingDirections()) {
            if (!direction.getAxis().isHorizontal()) continue;

            var state = defaultBlockState().setValue(FACING, direction.getOpposite()).setValue(WATERLOGGED, waterlogged);
            if (state.canSurvive(ctx.getLevel(), ctx.getClickedPos())) {
                return state;
            }
        }

        return null;
    }

    // Based on WallTorchBlock.getStateForNeighborUpdate
    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            tickView.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }

        return state.getValue(FACING).getOpposite() == direction && !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState() : state;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        var be = world.getBlockEntity(pos);
        if (!(be instanceof Container inventory)) return InteractionResult.TRY_WITH_EMPTY_HAND;

        int slot = getSlot(state, hit);
        var existing = inventory.getItem(slot);

        if (existing.isEmpty()) {
            if (!stack.isEmpty()) {
                var copy = stack.copy();
                copy.setCount(1);
                inventory.setItem(slot, copy);
                be.setChanged();
                if (!world.isClientSide()) {
                    PlatformBridges.get().getNetwork().syncBlockEntity(be);
                    player.awardStat(AdornStats.INTERACT_WITH_SHELF);
                }

                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }
        } else {
            if (!world.isClientSide()) {
                if (player.getItemInHand(hand).isEmpty()) {
                    player.setItemInHand(hand, existing);
                } else {
                    Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), existing);
                }
            }
            inventory.setItem(slot, ItemStack.EMPTY);
            be.setChanged();
            if (!world.isClientSide()) {
                PlatformBridges.get().getNetwork().syncBlockEntity(be);
                player.awardStat(AdornStats.INTERACT_WITH_SHELF);
            }
        }

        return InteractionResult.SUCCESS;
    }

    /**
     * Returns the slot that the player hit or -1 if it's not available.
     */
    private int getSlot(BlockState state, BlockHitResult hitResult) {
        var blockPos = hitResult.getBlockPos();
        var pos = hitResult.getLocation();
        var xo = pos.x - blockPos.getX();
        var zo = pos.z - blockPos.getZ();
        var facing = state.getValue(FACING);
        var side = hitResult.getDirection();

        if (side == facing || side == Direction.UP || side == Direction.DOWN || side == facing.getOpposite()) {
            return switch (facing) {
                case EAST -> zo <= 0.5 ? 0 : 1;
                case WEST -> zo <= 0.5 ? 1 : 0;
                case NORTH -> xo <= 0.5 ? 0 : 1;
                case SOUTH -> xo <= 0.5 ? 1 : 0;
                default -> -1;
            };
        } else if (side == facing.getCounterClockWise()) {
            // Right side of shelf
            return 1;
        } else if (side == facing.getClockWise()) {
            // Left side of shelf
            return 0;
        } else {
            return -1;
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel world, BlockPos pos, boolean moved) {
        Containers.updateNeighboursAfterDestroy(state, world, pos);
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
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos, Direction direction) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos));
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return AdornBlockEntities.SHELF.get().create(pos, state);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        throw new UnsupportedOperationException();
    }
}
