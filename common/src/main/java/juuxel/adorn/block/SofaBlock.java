package juuxel.adorn.block;

import juuxel.adorn.block.property.FrontConnection;
import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.lib.AdornStats;
import juuxel.adorn.util.Shapes;
import juuxel.adorn.util.verification.UsesBlockProperty;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@UsesBlockProperty(Direction.class)
public class SofaBlock extends SeatBlock implements Waterloggable, SneakClickHandler, BlockWithDescription {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty CONNECTED_LEFT = BooleanProperty.of("connected_left");
    public static final BooleanProperty CONNECTED_RIGHT = BooleanProperty.of("connected_right");
    public static final EnumProperty<FrontConnection> FRONT_CONNECTION = EnumProperty.of("front", FrontConnection.class);
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    private static final VoxelShape[] OUTLINE_SHAPES = buildShapes(false);
    private static final VoxelShape[] COLLISION_SHAPES = buildShapes(true);
    private static final String DESCRIPTION_KEY = "block.adorn.sofa.description";

    public SofaBlock(BlockVariant variant) {
        super(variant.createSettings());
        setDefaultState(getDefaultState()
            .with(FRONT_CONNECTION, FrontConnection.NONE)
            .with(CONNECTED_LEFT, false)
            .with(CONNECTED_RIGHT, false)
            .with(WATERLOGGED, false));
    }

    @Override
    public Identifier getSittingStat() {
        return AdornStats.SIT_ON_SOFA;
    }

    @Override
    public String getDescriptionKey() {
        return DESCRIPTION_KEY;
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (stack.getItem() instanceof DyeItem dye) {
            world.setBlockState(pos, AdornBlocks.SOFAS.getEager(dye.getColor()).getStateWithProperties(state));
            world.playSound(player, pos, SoundEvents.BLOCK_WOOL_PLACE, SoundCategory.BLOCKS, 1f, 0.8f);
            if (!player.getAbilities().creativeMode) stack.decrement(1);
            if (!world.isClient) player.incrementStat(AdornStats.DYE_SOFA);
            return ItemActionResult.success(world.isClient);
        }

        return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    }

    @Override
    public ActionResult onSneakClick(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        var sleepingDirection = getSleepingDirection(world, pos);

        if (state.get(OCCUPIED)) {
            player.sendMessage(Text.translatable("block.adorn.sofa.occupied"), true);
            return ActionResult.success(world.isClient);
        }

        if (BedBlock.isBedWorking(world) && sleepingDirection != null) {
            if (!world.isClient) {
                player.trySleep(pos).ifLeft(reason -> {
                    if (reason.getMessage() != null) {
                        player.sendMessage(reason.getMessage(), true);
                    }
                });
            }

            return ActionResult.success(world.isClient);
        } else {
            return ActionResult.PASS;
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING, CONNECTED_LEFT, CONNECTED_RIGHT, FRONT_CONNECTION, WATERLOGGED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return updateConnections(
            getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite())
                .with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER),
            ctx.getWorld(),
            ctx.getBlockPos()
        );
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return updateConnections(state, world, pos);
    }

    private BlockState updateConnections(BlockState state, WorldAccess world, BlockPos pos) {
        var direction = state.get(FACING);
        var leftState = world.getBlockState(pos.offset(direction.rotateYClockwise()));
        var rightState = world.getBlockState(pos.offset(direction.rotateYCounterclockwise()));
        var frontState = world.getBlockState(pos.offset(direction));

        var connectedLeft = leftState.getBlock() instanceof SofaBlock && (leftState.get(FACING) == direction || (leftState.get(FACING) == direction.rotateYCounterclockwise() && leftState.get(FRONT_CONNECTION) != FrontConnection.NONE));
        var connectedRight = rightState.getBlock() instanceof SofaBlock && (rightState.get(FACING) == direction || (rightState.get(FACING) == direction.rotateYClockwise() && rightState.get(FRONT_CONNECTION) != FrontConnection.NONE));
        var connectedFront = frontState.getBlock() instanceof SofaBlock;
        var connectedFrontLeft = connectedFront && !connectedLeft && frontState.get(FACING) == direction.rotateYCounterclockwise();
        var connectedFrontRight = connectedFront && !connectedRight && frontState.get(FACING) == direction.rotateYClockwise();
        var frontConnection = FrontConnection.NONE;
        if (connectedFrontLeft) {
            frontConnection = FrontConnection.LEFT;
        } else if (connectedFrontRight) {
            frontConnection = FrontConnection.RIGHT;
        }

        return state
            .with(CONNECTED_LEFT, connectedLeft)
            .with(CONNECTED_RIGHT, connectedRight)
            .with(FRONT_CONNECTION, frontConnection);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return OUTLINE_SHAPES[
            getShapeKey(
                state.get(FACING),
                state.get(CONNECTED_LEFT),
                state.get(CONNECTED_RIGHT),
                state.get(FRONT_CONNECTION)
            )
        ];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return COLLISION_SHAPES[
            getShapeKey(
                state.get(FACING),
                state.get(CONNECTED_LEFT),
                state.get(CONNECTED_RIGHT),
                state.get(FRONT_CONNECTION)
            )
        ];
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
    public boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }

    @Override
    public double getSittingOffset(World world, BlockState state, BlockPos pos) {
        return 0.4375; // 7/16
    }

    @Override
    public Direction getPreferredDismountDirection(BlockState state, Entity passenger) {
        return state.get(FACING);
    }

    private static VoxelShape[] buildShapes(boolean thin) {
        var bottom = createCuboidShape(0.0, 2.0, 0.0, 16.0, 7.0, 16.0);
        var leftArms = Shapes.buildShapeRotations(5, 7, 13, 16, 13, 16);
        var rightArms = Shapes.buildShapeRotations(5, 7, 0, 16, 13, 3);
        var thinLeftArms = Shapes.buildShapeRotations(5, 7, 14, 16, 13, 16);
        var thinRightArms = Shapes.buildShapeRotations(5, 7, 0, 16, 13, 2);
        var backs = Shapes.buildShapeRotations(0, 7, 0, 5, 16, 16);
        var leftCorners = Shapes.buildShapeRotations(5, 7, 11, 16, 16, 16);
        var rightCorners = Shapes.buildShapeRotations(5, 7, 0, 16, 16, 5);
        var booleans = new boolean[] { true, false };
        var result = new VoxelShape[48];
        for (var facing : FACING.getValues()) {
            for (var left : booleans) {
                for (var right : booleans) {
                    for (var front : FRONT_CONNECTION.getValues()) {
                        List<VoxelShape> parts = new ArrayList<>();
                        parts.add(backs.get(facing));

                        if (!left && front == FrontConnection.NONE) {
                            parts.add(thin ? thinLeftArms.get(facing) : leftArms.get(facing));
                        }
                        if (!right && front == FrontConnection.NONE) {
                            parts.add(thin ? thinRightArms.get(facing) : rightArms.get(facing));
                        }

                        switch (front) {
                            case LEFT -> parts.add(leftCorners.get(facing));
                            case RIGHT -> parts.add(rightCorners.get(facing));
                        }

                        int key = getShapeKey(facing, left, right, front);
                        var shape = VoxelShapes.union(bottom, parts.toArray(VoxelShape[]::new));
                        result[key] = shape;
                    }
                }
            }
        }
        return result;
    }

    private static int getShapeKey(Direction facing, boolean left, boolean right, FrontConnection front) {
        return front.ordinal() << 4 | (left ? 1 : 0) << 3 | (right ? 1 : 0) << 2 | facing.getHorizontal();
    }

    public static @Nullable Direction getSleepingDirection(BlockView world, BlockPos pos) {
        return getSleepingDirection(world, pos, false);
    }

    // TODO: Is the ignoreNeighbors = false state ever needed??
    public static @Nullable Direction getSleepingDirection(BlockView world, BlockPos pos, boolean ignoreNeighbors) {
        var state = world.getBlockState(pos);
        if (!(state.getBlock() instanceof SofaBlock)) return null;

        boolean connectedLeft = state.get(CONNECTED_LEFT);
        boolean connectedRight = state.get(CONNECTED_RIGHT);
        var frontConnection = state.get(FRONT_CONNECTION);
        var facing = state.get(FACING);

        if ((!connectedLeft && !connectedRight && frontConnection == FrontConnection.NONE) || (!ignoreNeighbors && state.get(OCCUPIED))) {
            return null;
        }

        @Nullable Direction result;
        if (frontConnection != FrontConnection.NONE) {
            result = facing;
        } else if (connectedLeft) {
            result = facing.rotateYClockwise();
        } else if (connectedRight) {
            result = facing.rotateYCounterclockwise();
        } else {
            result = null;
        }

        if (result != null) {
            if (ignoreNeighbors) {
                return result;
            }
            var neighborState = world.getBlockState(pos.offset(result));
            if (neighborState.getBlock() instanceof SofaBlock && !neighborState.get(OCCUPIED)) {
                return result;
            }
        }

        return null;
    }
}
