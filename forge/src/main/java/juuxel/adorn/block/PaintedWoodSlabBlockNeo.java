package juuxel.adorn.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;

public final class PaintedWoodSlabBlockNeo extends SlabBlock {
    public PaintedWoodSlabBlockNeo(Properties settings) {
        super(settings);
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return PaintedPlanksBlockNeo.BURN_CHANCE;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return PaintedPlanksBlockNeo.SPREAD_CHANCE;
    }
}
