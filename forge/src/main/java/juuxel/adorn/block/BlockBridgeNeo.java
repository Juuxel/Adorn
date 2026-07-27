package juuxel.adorn.block;

import juuxel.adorn.platform.BlockBridge;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public final class BlockBridgeNeo implements BlockBridge {
    @Override
    public boolean hasNextOxidationLevelForTicking(BlockState state) {
        // This works assuming the data maps actually do define oxidation for this block.
        // The vanilla code (see the Fabric impl) won't work since this needs to return a suitable value
        // before the neoforge:oxidizables data map is loaded.
        return state.getBlock() instanceof WeatheringCopper ox && ox.getAge() != WeatheringCopper.WeatherState.OXIDIZED;
    }

    @Override
    public float getSlipperiness(BlockState state, LevelReader world, BlockPos pos, @Nullable Entity entity) {
        return state.getFriction(world, pos, entity);
    }
}
