package juuxel.adorn.block;

import juuxel.adorn.block.property.FrontConnection;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;

public class KitchenCounterBlock extends AbstractKitchenCounterBlock implements BlockWithDescription {
    public static final EnumProperty<FrontConnection> FRONT = EnumProperty.create("front", FrontConnection.class);
    private static final String DESCRIPTION_KEY = "block.adorn.kitchen_counter.description";

    public KitchenCounterBlock(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState().setValue(FRONT, FrontConnection.NONE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FRONT);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return updateConnections(super.getStateForPlacement(ctx), ctx.getLevel(), ctx.getClickedPos());
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        return updateConnections(state, world, pos);
    }

    private BlockState updateConnections(BlockState state, BlockGetter world, BlockPos pos) {
        var facing = state.getValue(FACING);
        var frontState = world.getBlockState(pos.relative(facing));
        var frontConnection = FrontConnection.NONE;

        if (frontState.getBlock() instanceof AbstractKitchenCounterBlock) {
            var frontFacing = frontState.getValue(FACING);
            if (frontFacing == facing.getClockWise()) {
                frontConnection = FrontConnection.LEFT;
            } else if (frontFacing == facing.getCounterClockWise()) {
                frontConnection = FrontConnection.RIGHT;
            }
        }

        return state.setValue(FRONT, frontConnection);
    }

    @Override
    public String getDescriptionKey() {
        return DESCRIPTION_KEY;
    }
}
