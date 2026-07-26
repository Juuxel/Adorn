package juuxel.adorn.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;

public final class ChainLinkFenceBlock extends IronBarsBlock implements BlockWithDescription {
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;

    public ChainLinkFenceBlock(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState().setValue(UP, false).setValue(DOWN, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(UP, DOWN);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        var state = super.getStateForPlacement(ctx);
        var world = ctx.getLevel();
        var pos = ctx.getClickedPos();

        return state
            .setValue(UP, connectsVerticallyTo(world.getBlockState(pos.above())))
            .setValue(DOWN, connectsVerticallyTo(world.getBlockState(pos.below())));
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        var result = super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random);

        if (direction == Direction.UP) {
            result = result.setValue(UP, connectsVerticallyTo(neighborState));
        } else if (direction == Direction.DOWN) {
            result = result.setValue(DOWN, connectsVerticallyTo(neighborState));
        }

        return result;
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
        return false;
    }

    private static boolean connectsVerticallyTo(BlockState state) {
        return state.getBlock() instanceof ChainLinkFenceBlock;
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    public boolean attachsTo(BlockState state, boolean sideSolidFullSquare) {
        return super.attachsTo(state, sideSolidFullSquare) || state.is(BlockTags.FENCE_GATES);
    }
}
