package juuxel.adorn.block;

import com.mojang.serialization.MapCodec;
import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.lib.AdornStats;
import juuxel.adorn.platform.PlatformBridges;
import juuxel.adorn.util.Shapes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.menu.Menu;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public final class ShelfBlock extends VisibleBlockWithEntity implements Waterloggable, BlockWithDescription {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    private static final String DESCRIPTION_KEY = "block.adorn.shelf.description";
    private static final Map<Direction, VoxelShape> SHAPES = Shapes.buildShapeRotations(0, 5, 0, 7, 6, 16);

    public ShelfBlock(BlockVariant variant) {
        super(variant.createSettings());
        setDefaultState(getDefaultState().with(WATERLOGGED, false));
    }

    @Override
    public String getDescriptionKey() {
        return DESCRIPTION_KEY;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES.get(state.get(FACING));
    }

    // Based on WallTorchBlock.canPlaceAt
    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        var facing = state.get(FACING);
        var neighborPos = pos.offset(facing.getOpposite());
        return world.getBlockState(neighborPos).isSideSolidFullSquare(world, neighborPos, facing);
    }

    // Based on WallTorchBlock.getPlacementState
    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        var waterlogged = ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER;
        for (var direction : ctx.getPlacementDirections()) {
            if (!direction.getAxis().isHorizontal()) continue;

            var state = getDefaultState().with(FACING, direction.getOpposite()).with(WATERLOGGED, waterlogged);
            if (state.canPlaceAt(ctx.getWorld(), ctx.getBlockPos())) {
                return state;
            }
        }

        return null;
    }

    // Based on WallTorchBlock.getStateForNeighborUpdate
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return state.get(FACING).getOpposite() == direction && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : state;
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        var be = world.getBlockEntity(pos);
        if (!(be instanceof Inventory inventory)) return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        int slot = getSlot(state, hit);
        var existing = inventory.getStack(slot);

        if (existing.isEmpty()) {
            if (!stack.isEmpty()) {
                var copy = stack.copy();
                copy.setCount(1);
                inventory.setStack(slot, copy);
                be.markDirty();
                if (!world.isClient) {
                    PlatformBridges.get().getNetwork().syncBlockEntity(be);
                    player.incrementStat(AdornStats.INTERACT_WITH_SHELF);
                }

                if (!player.getAbilities().creativeMode) {
                    stack.decrement(1);
                }
            }
        } else {
            if (!world.isClient) {
                if (player.getStackInHand(hand).isEmpty()) {
                    player.setStackInHand(hand, existing);
                } else {
                    ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), existing);
                }
            }
            inventory.setStack(slot, ItemStack.EMPTY);
            be.markDirty();
            if (!world.isClient) {
                PlatformBridges.get().getNetwork().syncBlockEntity(be);
                player.incrementStat(AdornStats.INTERACT_WITH_SHELF);
            }
        }

        return ItemActionResult.success(world.isClient);
    }

    /**
     * Returns the slot that the player hit or -1 if it's not available.
     */
    private int getSlot(BlockState state, BlockHitResult hitResult) {
        var blockPos = hitResult.getBlockPos();
        var pos = hitResult.getPos();
        var xo = pos.x - blockPos.getX();
        var zo = pos.z - blockPos.getZ();
        var facing = state.get(FACING);
        var side = hitResult.getSide();

        if (side == facing || side == Direction.UP || side == Direction.DOWN || side == facing.getOpposite()) {
            return switch (facing) {
                case EAST -> zo <= 0.5 ? 0 : 1;
                case WEST -> zo <= 0.5 ? 1 : 0;
                case NORTH -> xo <= 0.5 ? 0 : 1;
                case SOUTH -> xo <= 0.5 ? 1 : 0;
                default -> -1;
            };
        } else if (side == facing.rotateYCounterclockwise()) {
            // Right side of shelf
            return 1;
        } else if (side == facing.rotateYClockwise()) {
            // Left side of shelf
            return 0;
        } else {
            return -1;
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        ItemScatterer.onStateReplaced(state, newState, world, pos);
        super.onStateReplaced(state, world, pos, newState, moved);
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
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return Menu.calculateComparatorOutput(world.getBlockEntity(pos));
    }

    @Override
    public boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return AdornBlockEntities.SHELF.get().instantiate(pos, state);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        throw new UnsupportedOperationException();
    }
}
