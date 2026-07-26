package juuxel.adorn.platform.fabric;

import juuxel.adorn.block.entity.BrewerBlockEntity;
import juuxel.adorn.block.entity.BrewerBlockEntityFabric;
import juuxel.adorn.block.entity.KitchenSinkBlockEntity;
import juuxel.adorn.block.entity.KitchenSinkBlockEntityFabric;
import juuxel.adorn.platform.BlockEntityBridge;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;

public final class BlockEntityBridgeFabric implements BlockEntityBridge {
    public static final BlockEntityBridgeFabric INSTANCE = new BlockEntityBridgeFabric();

    @Override
    public BrewerBlockEntity createBrewer(BlockPos pos, BlockState state) {
        return new BrewerBlockEntityFabric(pos, state);
    }

    @Override
    public KitchenSinkBlockEntity createKitchenSink(BlockPos pos, BlockState state) {
        return new KitchenSinkBlockEntityFabric(pos, state);
    }
}
