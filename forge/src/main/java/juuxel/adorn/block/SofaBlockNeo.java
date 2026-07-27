package juuxel.adorn.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

public final class SofaBlockNeo extends SofaBlock {
    public SofaBlockNeo(Properties settings) {
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
