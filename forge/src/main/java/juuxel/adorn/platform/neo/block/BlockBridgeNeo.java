package juuxel.adorn.platform.neo.block;

import juuxel.adorn.platform.BlockBridge;
import net.minecraft.block.BlockState;
import net.minecraft.block.Oxidizable;

public final class BlockBridgeNeo implements BlockBridge {
    @Override
    public boolean hasNextOxidationLevelForTicking(BlockState state) {
        // This works assuming the data maps actually do define oxidation for this block.
        // The vanilla code (see the Fabric impl) won't work since this needs to return a suitable value
        // before the neoforge:oxidizables data map is loaded.
        return state.getBlock() instanceof Oxidizable ox && ox.getDegradationLevel() != Oxidizable.OxidationLevel.OXIDIZED;
    }
}
