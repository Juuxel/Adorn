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
import java.util.List;

public abstract class CarpetedBlock extends SeatBlock {
    public static final OptionalProperty<DyeColor> CARPET = new OptionalProperty<>(EnumProperty.create("carpet", DyeColor.class, Dyes.ALL_DYES));
    public static final VoxelShape CARPET_SHAPE = box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);

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
            var carpetBlock = getCarpetBlock(carpet.value());
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
            if (carpet.isPresent() && !getCarpetBlock(carpet.value()).defaultBlockState().canSurvive(world, pos)) {
                tickView.scheduleTick(pos, this, 1);
            }
        }

        return super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        if (isCarpetingEnabled() && state.getValue(CARPET).isPresent()) {
            var stacks = new ArrayList<>(super.getDrops(state, builder));
            stacks.addAll(getCarpetBlock(state.getValue(CARPET).value()).defaultBlockState().getDrops(builder));
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

    private static Block getCarpetBlock(DyeColor color) {
        return Blocks.CARPET.pick(color);
    }
}
