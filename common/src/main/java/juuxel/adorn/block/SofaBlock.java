package juuxel.adorn.block;

import juuxel.adorn.block.property.FrontConnection;
import juuxel.adorn.lib.AdornStats;
import juuxel.adorn.lib.AdornTags;
import juuxel.adorn.util.ShapeRotation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.attribute.BedRule;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
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
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SofaBlock extends SeatBlock implements SimpleWaterloggedBlock, SneakClickHandler, BlockWithDescription {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty CONNECTED_LEFT = BooleanProperty.create("connected_left");
    public static final BooleanProperty CONNECTED_RIGHT = BooleanProperty.create("connected_right");
    public static final EnumProperty<FrontConnection> FRONT_CONNECTION = EnumProperty.create("front", FrontConnection.class);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape[] OUTLINE_SHAPES = buildShapes(false);
    private static final VoxelShape[] COLLISION_SHAPES = buildShapes(true);
    private static final String DESCRIPTION_KEY = "block.adorn.sofa.description";

    public SofaBlock(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState()
            .setValue(FRONT_CONNECTION, FrontConnection.NONE)
            .setValue(CONNECTED_LEFT, false)
            .setValue(CONNECTED_RIGHT, false)
            .setValue(WATERLOGGED, false));
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
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (stack.is(AdornTags.FURNITURE_DYES)) {
            var dye = stack.get(DataComponents.DYE);
            if (dye != null) {
                world.setBlockAndUpdate(pos, AdornBlocks.SOFAS.pick(dye).get().withPropertiesOf(state));
                world.playSound(player, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1f, 0.8f);
                if (!player.getAbilities().instabuild) stack.shrink(1);
                if (!world.isClientSide()) player.awardStat(AdornStats.DYE_SOFA);
                return InteractionResult.SUCCESS;
            }
        }

        return super.useItemOn(stack, state, world, pos, player, hand, hit);
    }

    @Override
    public InteractionResult onSneakClick(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        var sleepingDirection = getSleepingDirection(world, pos);

        if (state.getValue(OCCUPIED)) {
            player.sendOverlayMessage(Component.translatable("block.adorn.sofa.occupied"));
            return InteractionResult.SUCCESS;
        }

        var bedRule = modifyBedRuleForSofas(
            world.environmentAttributes().getValue(EnvironmentAttributes.BED_RULE, pos)
        );
        if (bedRule.canSleep(world) && sleepingDirection != null) {
            if (!world.isClientSide()) {
                player.startSleepInBed(pos).ifLeft(reason -> {
                    if (reason.message() != null) {
                        player.sendOverlayMessage(reason.message());
                    }
                });
            }

            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    public static BedRule modifyBedRuleForSofas(BedRule rule) {
        return rule.canSleep() == BedRule.Rule.WHEN_DARK ? new BedRule(BedRule.Rule.ALWAYS, rule.canSetSpawn(), rule.explodes(), rule.errorMessage()) : rule;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, CONNECTED_LEFT, CONNECTED_RIGHT, FRONT_CONNECTION, WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return updateConnections(
            defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, ctx.getLevel().getFluidState(ctx.getClickedPos()).getType() == Fluids.WATER),
            ctx.getLevel(),
            ctx.getClickedPos()
        );
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            tickView.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }

        return updateConnections(state, world, pos);
    }

    private BlockState updateConnections(BlockState state, BlockGetter world, BlockPos pos) {
        var direction = state.getValue(FACING);
        var leftState = world.getBlockState(pos.relative(direction.getClockWise()));
        var rightState = world.getBlockState(pos.relative(direction.getCounterClockWise()));
        var frontState = world.getBlockState(pos.relative(direction));

        var connectedLeft = leftState.getBlock() instanceof SofaBlock && (leftState.getValue(FACING) == direction || (leftState.getValue(FACING) == direction.getCounterClockWise() && leftState.getValue(FRONT_CONNECTION) != FrontConnection.NONE));
        var connectedRight = rightState.getBlock() instanceof SofaBlock && (rightState.getValue(FACING) == direction || (rightState.getValue(FACING) == direction.getClockWise() && rightState.getValue(FRONT_CONNECTION) != FrontConnection.NONE));
        var connectedFront = frontState.getBlock() instanceof SofaBlock;
        var connectedFrontLeft = connectedFront && !connectedLeft && frontState.getValue(FACING) == direction.getCounterClockWise();
        var connectedFrontRight = connectedFront && !connectedRight && frontState.getValue(FACING) == direction.getClockWise();
        var frontConnection = FrontConnection.NONE;
        if (connectedFrontLeft) {
            frontConnection = FrontConnection.LEFT;
        } else if (connectedFrontRight) {
            frontConnection = FrontConnection.RIGHT;
        }

        return state
            .setValue(CONNECTED_LEFT, connectedLeft)
            .setValue(CONNECTED_RIGHT, connectedRight)
            .setValue(FRONT_CONNECTION, frontConnection);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return OUTLINE_SHAPES[
            getShapeKey(
                state.getValue(FACING),
                state.getValue(CONNECTED_LEFT),
                state.getValue(CONNECTED_RIGHT),
                state.getValue(FRONT_CONNECTION)
            )
        ];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return COLLISION_SHAPES[
            getShapeKey(
                state.getValue(FACING),
                state.getValue(CONNECTED_LEFT),
                state.getValue(CONNECTED_RIGHT),
                state.getValue(FRONT_CONNECTION)
            )
        ];
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
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    public double getSittingOffset(Level world, BlockState state, BlockPos pos) {
        return 0.4375; // 7/16
    }

    @Override
    public Direction getPreferredDismountDirection(BlockState state, Entity passenger) {
        return state.getValue(FACING);
    }

    private static VoxelShape[] buildShapes(boolean thin) {
        var bottom = box(0.0, 2.0, 0.0, 16.0, 7.0, 16.0);
        var leftArms = ShapeRotation.buildShapeRotations(5, 7, 13, 16, 13, 16);
        var rightArms = ShapeRotation.buildShapeRotations(5, 7, 0, 16, 13, 3);
        var thinLeftArms = ShapeRotation.buildShapeRotations(5, 7, 14, 16, 13, 16);
        var thinRightArms = ShapeRotation.buildShapeRotations(5, 7, 0, 16, 13, 2);
        var backs = ShapeRotation.buildShapeRotations(0, 7, 0, 5, 16, 16);
        var leftCorners = ShapeRotation.buildShapeRotations(5, 7, 11, 16, 16, 16);
        var rightCorners = ShapeRotation.buildShapeRotations(5, 7, 0, 16, 16, 5);
        var booleans = new boolean[] { true, false };
        var result = new VoxelShape[48];
        for (var facing : FACING.getPossibleValues()) {
            for (var left : booleans) {
                for (var right : booleans) {
                    for (var front : FRONT_CONNECTION.getPossibleValues()) {
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
                        var shape = Shapes.or(bottom, parts.toArray(VoxelShape[]::new));
                        result[key] = shape;
                    }
                }
            }
        }
        return result;
    }

    private static int getShapeKey(Direction facing, boolean left, boolean right, FrontConnection front) {
        return front.ordinal() << 4 | (left ? 1 : 0) << 3 | (right ? 1 : 0) << 2 | facing.get2DDataValue();
    }

    public static @Nullable Direction getSleepingDirection(BlockGetter world, BlockPos pos) {
        return getSleepingDirection(world, pos, false);
    }

    // TODO: Is the ignoreNeighbors = false state ever needed??
    public static @Nullable Direction getSleepingDirection(BlockGetter world, BlockPos pos, boolean ignoreNeighbors) {
        var state = world.getBlockState(pos);
        if (!(state.getBlock() instanceof SofaBlock)) return null;

        boolean connectedLeft = state.getValue(CONNECTED_LEFT);
        boolean connectedRight = state.getValue(CONNECTED_RIGHT);
        var frontConnection = state.getValue(FRONT_CONNECTION);
        var facing = state.getValue(FACING);

        if ((!connectedLeft && !connectedRight && frontConnection == FrontConnection.NONE) || (!ignoreNeighbors && state.getValue(OCCUPIED))) {
            return null;
        }

        Direction result;
        if (frontConnection != FrontConnection.NONE) {
            result = facing;
        } else if (connectedLeft) {
            result = facing.getClockWise();
        } else if (connectedRight) {
            result = facing.getCounterClockWise();
        } else {
            result = null;
        }

        if (result != null) {
            if (ignoreNeighbors) {
                return result;
            }
            var neighborState = world.getBlockState(pos.relative(result));
            if (neighborState.getBlock() instanceof SofaBlock && !neighborState.getValue(OCCUPIED)) {
                return result;
            }
        }

        return null;
    }
}
