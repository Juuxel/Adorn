package juuxel.adorn.platform.fabric;

import juuxel.adorn.platform.BlockBridge;
import net.minecraft.block.BlockState;
import net.minecraft.block.Oxidizable;

public final class BlockBridgeFabric implements BlockBridge {
    @Override
    public boolean hasNextOxidationLevelForTicking(BlockState state) {
        return Oxidizable.getIncreasedOxidationBlock(state.getBlock()).isPresent();
    }
}
