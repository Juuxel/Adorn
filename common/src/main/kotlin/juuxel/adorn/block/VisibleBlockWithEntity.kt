package juuxel.adorn.block

import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity

abstract class VisibleBlockWithEntity(settings: Settings) : BlockWithEntity(settings) {
    override fun getRenderType(state: BlockState) = BlockRenderType.MODEL
}
