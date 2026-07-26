package juuxel.adorn.platform.forge.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;

public final class PaintedPlanksBlockNeo extends Block {
    public static final int BURN_CHANCE = 5;
    public static final int SPREAD_CHANCE = 20;

    public PaintedPlanksBlockNeo(Properties settings) {
        super(settings);
    }

    // getFireSpreadSpeed = burnChance
    // getFlammability = spreadChance

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return BURN_CHANCE;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return SPREAD_CHANCE;
    }
}
