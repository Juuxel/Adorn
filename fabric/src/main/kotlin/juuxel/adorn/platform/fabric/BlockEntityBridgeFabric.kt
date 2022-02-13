package juuxel.adorn.platform.fabric

import juuxel.adorn.block.entity.BrewerBlockEntity
import juuxel.adorn.block.entity.KitchenSinkBlockEntity
import juuxel.adorn.block.entity.KitchenSinkBlockEntityFabric
import juuxel.adorn.platform.BlockEntityBridge
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

object BlockEntityBridgeFabric : BlockEntityBridge {
    override fun createBrewer(pos: BlockPos, state: BlockState): BrewerBlockEntity =
        BrewerBlockEntity(pos, state)

    override fun createKitchenSink(pos: BlockPos, state: BlockState): KitchenSinkBlockEntity =
        KitchenSinkBlockEntityFabric(pos, state)
}
