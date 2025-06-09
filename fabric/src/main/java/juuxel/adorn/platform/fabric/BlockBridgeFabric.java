package juuxel.adorn.platform.fabric;

import juuxel.adorn.platform.BlockBridge;
import net.minecraft.block.BlockState;
import net.minecraft.block.Oxidizable;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public final class BlockBridgeFabric implements BlockBridge {
    @Override
    public boolean hasNextOxidationLevelForTicking(BlockState state) {
        return Oxidizable.getIncreasedOxidationBlock(state.getBlock()).isPresent();
    }

    @Override
    public float getSlipperiness(BlockState state, WorldView world, BlockPos pos, @Nullable Entity entity) {
        return state.getBlock().getSlipperiness();
    }
}
