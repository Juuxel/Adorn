package juuxel.adorn.block;

import juuxel.adorn.block.property.OptionalProperty;
import juuxel.adorn.util.Dyes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WoolCarpetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public abstract class CarpetedBlock extends SeatBlock {
    public static final OptionalProperty<DyeColor> CARPET = new OptionalProperty<>(EnumProperty.create("carpet", DyeColor.class, Dyes.ALL_DYES));
    public static final VoxelShape CARPET_SHAPE = box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
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

    public CarpetedBlock(Properties settings) {
        super(settings);

        if (isCarpetingEnabled()) {
            registerDefaultState(defaultBlockState().setValue(CARPET, CARPET.getNone()));
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        if (isCarpetingEnabled()) builder.add(CARPET);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        if (isCarpetingEnabled()) {
            return super.getStateForPlacement(ctx).setValue(CARPET, CARPET.wrapOrNone(getCarpetColor(ctx)));
        }

        return super.getStateForPlacement(ctx);
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!isCarpetingEnabled()) return;
        var carpet = state.getValue(CARPET);
        if (carpet.isPresent()) {
            var carpetBlock = COLORS_TO_BLOCKS.get(carpet.value());
            if (!carpetBlock.defaultBlockState().canSurvive(world, pos)) {
                carpetBlock.playerWillDestroy(world, pos, state, null);
                dropResources(carpetBlock.defaultBlockState(), world, pos);
                world.setBlockAndUpdate(pos, state.setValue(CARPET, CARPET.getNone()));
            }
        }
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (isCarpetingEnabled()) {
            var carpet = state.getValue(CARPET);
            if (carpet.isPresent() && !COLORS_TO_BLOCKS.get(carpet.value()).defaultBlockState().canSurvive(world, pos)) {
                tickView.scheduleTick(pos, this, 1);
            }
        }

        return super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        if (isCarpetingEnabled() && state.getValue(CARPET).isPresent()) {
            var stacks = new ArrayList<>(super.getDrops(state, builder));
            stacks.addAll(COLORS_TO_BLOCKS.get(state.getValue(CARPET).value()).defaultBlockState().getDrops(builder));
            return stacks;
        }

        return super.getDrops(state, builder);
    }

    public boolean isCarpetingEnabled() {
        return true;
    }

    public boolean canStateBeCarpeted(BlockState state) {
        return isCarpetingEnabled() && state.getValue(CARPET) == CARPET.getNone();
    }

    private static @Nullable DyeColor getCarpetColor(BlockPlaceContext context) {
        var block = context.getLevel().getBlockState(context.getClickedPos()).getBlock();
        return block instanceof WoolCarpetBlock carpet ? carpet.getColor() : null;
    }
}
