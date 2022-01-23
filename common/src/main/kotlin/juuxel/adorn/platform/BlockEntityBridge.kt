package juuxel.adorn.platform

import juuxel.adorn.block.entity.BrewerBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

interface BlockEntityBridge {
    fun createBrewer(pos: BlockPos, state: BlockState): BrewerBlockEntity
}
