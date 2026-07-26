package juuxel.adorn.platform;

import juuxel.adorn.block.entity.BrewerBlockEntity;
import juuxel.adorn.block.entity.KitchenSinkBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;

public interface BlockEntityBridge {
    BrewerBlockEntity createBrewer(BlockPos pos, BlockState state);
    KitchenSinkBlockEntity createKitchenSink(BlockPos pos, BlockState state);
}
