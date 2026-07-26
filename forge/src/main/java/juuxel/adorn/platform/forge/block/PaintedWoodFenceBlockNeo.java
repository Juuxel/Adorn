package juuxel.adorn.platform.forge.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;

public final class PaintedWoodFenceBlockNeo extends FenceBlock {
    public PaintedWoodFenceBlockNeo(Properties settings) {
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
