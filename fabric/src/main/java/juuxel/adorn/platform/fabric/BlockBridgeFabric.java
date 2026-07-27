package juuxel.adorn.platform.fabric;

import juuxel.adorn.platform.BlockBridge;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public final class BlockBridgeFabric implements BlockBridge {
    @Override
    public boolean hasNextOxidationLevelForTicking(BlockState state) {
        return WeatheringCopper.getNext(state.getBlock()).isPresent();
    }

    @Override
    public float getSlipperiness(BlockState state, LevelReader world, BlockPos pos, @Nullable Entity entity) {
        return state.getBlock().getFriction();
    }
}
