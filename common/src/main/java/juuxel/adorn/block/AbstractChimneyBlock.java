package juuxel.adorn.block;

import juuxel.adorn.lib.AdornTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class AbstractChimneyBlock extends Block implements SimpleWaterloggedBlock {
    public static final BooleanProperty CONNECTED = BooleanProperty.create("connected");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape TOP_SHAPE = box(4.0, 0.0, 4.0, 12.0, 12.0, 12.0);
    private static final VoxelShape MIDDLE_SHAPE = box(5.0, 0.0, 5.0, 11.0, 16.0, 11.0);

    public AbstractChimneyBlock(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState()
            .setValue(CONNECTED, false)
            .setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CONNECTED, WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return updateConnections(
            defaultBlockState().setValue(WATERLOGGED, ctx.getLevel().getFluidState(ctx.getClickedPos()).getType() == Fluids.WATER),
            ctx.getLevel().getBlockState(ctx.getClickedPos().above())
        );
    }

    private BlockState updateConnections(BlockState state, BlockState neighborState) {
        return state.setValue(CONNECTED, neighborState.is(AdornTags.CHIMNEYS.block()));
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            tickView.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }

        return direction == Direction.UP ? updateConnections(state, neighborState) : state;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return state.getValue(CONNECTED) ? MIDDLE_SHAPE : TOP_SHAPE;
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    public static Properties createBlockSettings(MapColor color) {
        return createBlockSettings(color, 2f);
    }

    public static Properties createBlockSettings(MapColor color, float hardness) {
        return Properties.of()
            .mapColor(color)
            .forceSolidOn()
            .requiresCorrectToolForDrops()
            .strength(hardness, 6f)
            .randomTicks()
            .noOcclusion();
    }
}
