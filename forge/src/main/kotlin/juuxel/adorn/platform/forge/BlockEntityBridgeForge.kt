package juuxel.adorn.platform.forge

import juuxel.adorn.block.entity.BrewerBlockEntity
import juuxel.adorn.platform.BlockEntityBridge
import juuxel.adorn.platform.forge.block.entity.BrewerBlockEntityForge
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

object BlockEntityBridgeForge : BlockEntityBridge {
    override fun createBrewer(pos: BlockPos, state: BlockState): BrewerBlockEntity =
        BrewerBlockEntityForge(pos, state)
}
