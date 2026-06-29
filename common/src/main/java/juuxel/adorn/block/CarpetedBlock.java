package juuxel.adorn.block;

import juuxel.adorn.block.property.OptionalProperty;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DyedCarpetBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public abstract class CarpetedBlock extends SeatBlock {
    public static final OptionalProperty<DyeColor> CARPET = new OptionalProperty<>(EnumProperty.of("carpet", DyeColor.class));
    public static final VoxelShape CARPET_SHAPE = createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
    private static final Map<DyeColor, Block> COLORS_TO_BLOCKS = new EnumMap<>(DyeColor.class);

    static {
        COLORS_TO_BLOCKS.put(DyeColor.WHITE, Blocks.WHITE_CARPET);
        COLORS_TO_BLOCKS.put(DyeColor.ORANGE, Blocks.ORANGE_CARPET);
        COLORS_TO_BLOCKS.put(DyeColor.MAGENTA, Blocks.MAGENTA_CARPET);
        COLORS_TO_BLOCKS.put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_CARPET);
        COLORS_TO_BLOCKS.put(DyeColor.YELLOW, Blocks.YELLOW_CARPET);
        COLORS_TO_BLOCKS.put(DyeColor.LIME, Blocks.LIME_CARPET);
        COLORS_TO_BLOCKS.put(DyeColor.PINK, Blocks.PINK_CARPET);
        COLORS_TO_BLOCKS.put(DyeColor.GRAY, Blocks.GRAY_CARPET);
        COLORS_TO_BLOCKS.put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_CARPET);
        COLORS_TO_BLOCKS.put(DyeColor.CYAN, Blocks.CYAN_CARPET);
        COLORS_TO_BLOCKS.put(DyeColor.PURPLE, Blocks.PURPLE_CARPET);
        COLORS_TO_BLOCKS.put(DyeColor.BLUE, Blocks.BLUE_CARPET);
        COLORS_TO_BLOCKS.put(DyeColor.BROWN, Blocks.BROWN_CARPET);
        COLORS_TO_BLOCKS.put(DyeColor.GREEN, Blocks.GREEN_CARPET);
        COLORS_TO_BLOCKS.put(DyeColor.RED, Blocks.RED_CARPET);
        COLORS_TO_BLOCKS.put(DyeColor.BLACK, Blocks.BLACK_CARPET);
    }

    public CarpetedBlock(Settings settings) {
        super(settings);

        if (isCarpetingEnabled()) {
            setDefaultState(getDefaultState().with(CARPET, CARPET.getNone()));
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        if (isCarpetingEnabled()) builder.add(CARPET);
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        if (isCarpetingEnabled()) {
            return super.getPlacementState(ctx).with(CARPET, CARPET.wrapOrNone(getCarpetColor(ctx)));
        }

        return super.getPlacementState(ctx);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!isCarpetingEnabled()) return;
        var carpet = state.get(CARPET);
        if (carpet.isPresent()) {
            var carpetBlock = COLORS_TO_BLOCKS.get(carpet.value());
            if (!carpetBlock.getDefaultState().canPlaceAt(world, pos)) {
                carpetBlock.onBreak(world, pos, state, null);
                dropStacks(carpetBlock.getDefaultState(), world, pos);
                world.setBlockState(pos, state.with(CARPET, CARPET.getNone()));
            }
        }
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        if (isCarpetingEnabled()) {
            var carpet = state.get(CARPET);
            if (carpet.isPresent() && !COLORS_TO_BLOCKS.get(carpet.value()).getDefaultState().canPlaceAt(world, pos)) {
                tickView.scheduleBlockTick(pos, this, 1);
            }
        }

        return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected List<ItemStack> getDroppedStacks(BlockState state, LootWorldContext.Builder builder) {
        if (isCarpetingEnabled() && state.get(CARPET).isPresent()) {
            var stacks = new ArrayList<>(super.getDroppedStacks(state, builder));
            stacks.addAll(COLORS_TO_BLOCKS.get(state.get(CARPET).value()).getDefaultState().getDroppedStacks(builder));
            return stacks;
        }

        return super.getDroppedStacks(state, builder);
    }

    public boolean isCarpetingEnabled() {
        return true;
    }

    public boolean canStateBeCarpeted(BlockState state) {
        return isCarpetingEnabled() && state.get(CARPET) == CARPET.getNone();
    }

    private static @Nullable DyeColor getCarpetColor(ItemPlacementContext context) {
        var block = context.getWorld().getBlockState(context.getBlockPos()).getBlock();
        return block instanceof DyedCarpetBlock carpet ? carpet.getDyeColor() : null;
    }
}
