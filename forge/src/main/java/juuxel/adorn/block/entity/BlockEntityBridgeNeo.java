package juuxel.adorn.block.entity;

import juuxel.adorn.platform.BlockEntityBridge;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public final class BlockEntityBridgeNeo implements BlockEntityBridge {
    public static final BlockEntityBridgeNeo INSTANCE = new BlockEntityBridgeNeo();

    @Override
    public BrewerBlockEntity createBrewer(BlockPos pos, BlockState state) {
        return new BrewerBlockEntityNeo(pos, state);
    }

    @Override
    public KitchenSinkBlockEntity createKitchenSink(BlockPos pos, BlockState state) {
        return new KitchenSinkBlockEntityNeo(pos, state);
    }
}
