package juuxel.adorn.platform.forge;

import juuxel.adorn.block.entity.BrewerBlockEntity;
import juuxel.adorn.block.entity.KitchenSinkBlockEntity;
import juuxel.adorn.platform.BlockEntityBridge;
import juuxel.adorn.platform.forge.block.entity.BrewerBlockEntityForge;
import juuxel.adorn.platform.forge.block.entity.KitchenSinkBlockEntityForge;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;

public final class BlockEntityBridgeForge implements BlockEntityBridge {
    public static final BlockEntityBridgeForge INSTANCE = new BlockEntityBridgeForge();

    @Override
    public BrewerBlockEntity createBrewer(BlockPos pos, BlockState state) {
        return new BrewerBlockEntityForge(pos, state);
    }

    @Override
    public KitchenSinkBlockEntity createKitchenSink(BlockPos pos, BlockState state) {
        return new KitchenSinkBlockEntityForge(pos, state);
    }
}
