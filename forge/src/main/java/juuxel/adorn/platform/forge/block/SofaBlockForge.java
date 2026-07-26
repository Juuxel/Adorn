package juuxel.adorn.platform.forge.block;

import juuxel.adorn.block.SofaBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;

public final class SofaBlockForge extends SofaBlock {
    public SofaBlockForge(Properties settings) {
        super(settings);
    }

    @Override
    public boolean isBed(BlockState state, BlockGetter level, BlockPos pos, LivingEntity sleeper) {
        return true;
    }

    @Override
    public Direction getBedDirection(BlockState state, LevelReader world, BlockPos pos) {
        var direction = getSleepingDirection(world, pos);
        return direction != null ? direction.getOpposite() : null;
    }

    @Override
    public void setBedOccupied(BlockState state, Level world, BlockPos pos, LivingEntity sleeper, boolean occupied) {
        super.setBedOccupied(state, world, pos, sleeper, occupied);
        var neighborPos = pos.relative(getSleepingDirection(world, pos, true));
        world.setBlockAndUpdate(neighborPos, world.getBlockState(neighborPos).setValue(OCCUPIED, occupied));
    }
}
