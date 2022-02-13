package juuxel.adorn.platform.forge

import juuxel.adorn.block.entity.BrewerBlockEntity
import juuxel.adorn.block.entity.KitchenSinkBlockEntity
import juuxel.adorn.platform.BlockEntityBridge
import juuxel.adorn.platform.forge.block.entity.BrewerBlockEntityForge
import juuxel.adorn.platform.forge.block.entity.KitchenSinkBlockEntityForge
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

object BlockEntityBridgeForge : BlockEntityBridge {
    override fun createBrewer(pos: BlockPos, state: BlockState): BrewerBlockEntity =
        BrewerBlockEntityForge(pos, state)

    override fun createKitchenSink(pos: BlockPos, state: BlockState): KitchenSinkBlockEntity =
        KitchenSinkBlockEntityForge(pos, state)
}
