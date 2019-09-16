package juuxel.adorn.block

import juuxel.adorn.block.entity.BETypeProvider
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity

abstract class VisibleBlockWithEntity(settings: Settings) : BlockWithEntity(settings), BETypeProvider {
    override fun getRenderType(state: BlockState) = BlockRenderType.MODEL
}
