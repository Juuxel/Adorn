package juuxel.adorn.block;

import juuxel.adorn.lib.AdornStats;
import juuxel.adorn.util.Shapes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Map;

public final class TableLampBlock extends Block implements Waterloggable, BlockWithDescription {
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final BooleanProperty LIT = Properties.LIT;
    public static final DirectionProperty FACING = Properties.FACING;
    private static final String DESCRIPTION_KEY = "block.adorn.table_lamp.description";
    private static final Map<Direction, VoxelShape> SHAPES = Shapes.buildShapeRotationsFromNorth(3, 3, 2, 13, 13, 16);

    static {
        SHAPES.put(Direction.UP, createCuboidShape(
            3.0, 0.0, 3.0,
            13.0, 14.0, 13.0
        ));
        SHAPES.put(Direction.DOWN, createCuboidShape(
            3.0, 2.0, 3.0,
            13.0, 16.0, 13.0
        ));
    }

    public TableLampBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState()
            .with(LIT, true)
            .with(WATERLOGGED, false)
            .with(FACING, Direction.UP));
    }

    @Override
    public String getDescriptionKey() {
        return DESCRIPTION_KEY;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(LIT, WATERLOGGED, FACING);
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (stack.getItem() instanceof DyeItem dye) {
            world.setBlockState(pos, AdornBlocks.TABLE_LAMPS.getEager(dye.getColor()).getStateWithProperties(state));
            world.playSound(player, pos, SoundEvents.BLOCK_WOOL_PLACE, SoundCategory.BLOCKS, 1f, 0.8f);
            if (!player.getAbilities().creativeMode) stack.decrement(1);
            if (!world.isClient) player.incrementStat(AdornStats.DYE_TABLE_LAMP);
        } else {
            var wasLit = state.get(LIT);
            world.setBlockState(pos, state.with(LIT, !wasLit));
            var pitch = wasLit ? 0.5f : 0.6f;
            world.playSound(player, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3f, pitch);
            if (!world.isClient) player.incrementStat(AdornStats.INTERACT_WITH_TABLE_LAMP);
        }
        return ItemActionResult.success(world.isClient);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState()
            .with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER)
            .with(FACING, ctx.getSide());
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES.get(state.get(FACING));
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return state.get(LIT) ? 15 : 0;
    }

    @Override
    public boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    public static Settings createBlockSettings(DyeColor color) {
        return Settings.create()
            .mapColor(color)
            .solid()
            .hardness(0.3f)
            .resistance(0.3f)
            .sounds(BlockSoundGroup.WOOL)
            .luminance(state -> state.get(LIT) ? 15 : 0);
    }
}
