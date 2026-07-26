package juuxel.adorn.block;

import juuxel.adorn.lib.AdornStats;
import juuxel.adorn.util.ShapeRotation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;

import java.util.Map;

public final class TableLampBlock extends Block implements SimpleWaterloggedBlock, BlockWithDescription {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
    private static final String DESCRIPTION_KEY = "block.adorn.table_lamp.description";
    private static final Map<Direction, VoxelShape> SHAPES = ShapeRotation.buildShapeRotationsFromNorth(3, 3, 2, 13, 13, 16);

    static {
        SHAPES.put(Direction.UP, box(
            3.0, 0.0, 3.0,
            13.0, 14.0, 13.0
        ));
        SHAPES.put(Direction.DOWN, box(
            3.0, 2.0, 3.0,
            13.0, 16.0, 13.0
        ));
    }

    public TableLampBlock(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState()
            .setValue(LIT, true)
            .setValue(WATERLOGGED, false)
            .setValue(FACING, Direction.UP));
    }

    @Override
    public String getDescriptionKey() {
        return DESCRIPTION_KEY;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LIT, WATERLOGGED, FACING);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (stack.getItem() instanceof DyeItem dye) {
            world.setBlockAndUpdate(pos, AdornBlocks.TABLE_LAMPS.getEager(dye.getDyeColor()).withPropertiesOf(state));
            world.playSound(player, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1f, 0.8f);
            if (!player.getAbilities().instabuild) stack.shrink(1);
            if (!world.isClientSide()) player.awardStat(AdornStats.DYE_TABLE_LAMP);
        } else {
            var wasLit = state.getValue(LIT);
            world.setBlockAndUpdate(pos, state.setValue(LIT, !wasLit));
            var pitch = wasLit ? 0.5f : 0.6f;
            world.playSound(player, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3f, pitch);
            if (!world.isClientSide()) player.awardStat(AdornStats.INTERACT_WITH_TABLE_LAMP);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState()
            .setValue(WATERLOGGED, ctx.getLevel().getFluidState(ctx.getClickedPos()).getType() == Fluids.WATER)
            .setValue(FACING, ctx.getClickedFace());
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos, Direction direction) {
        return state.getValue(LIT) ? 15 : 0;
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            tickView.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }

        return super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    public static Properties createBlockSettings(DyeColor color) {
        return Properties.of()
            .mapColor(color)
            .forceSolidOn()
            .destroyTime(0.3f)
            .explosionResistance(0.3f)
            .sound(SoundType.WOOL)
            .lightLevel(state -> state.getValue(LIT) ? 15 : 0);
    }
}
