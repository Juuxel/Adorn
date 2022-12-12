package juuxel.adorn.platform

import juuxel.adorn.block.SofaBlock
import juuxel.adorn.block.variant.BlockVariant

interface BlockFactory {
    fun createSofa(variant: BlockVariant): SofaBlock = SofaBlock(variant)

    companion object Default : BlockFactory
}
