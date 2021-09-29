package juuxel.adorn.platform

import juuxel.adorn.api.block.BlockVariant
import juuxel.adorn.block.SofaBlock

interface BlockFactory {
    fun createSofa(variant: BlockVariant): SofaBlock = SofaBlock(variant)

    companion object Default : BlockFactory
}
