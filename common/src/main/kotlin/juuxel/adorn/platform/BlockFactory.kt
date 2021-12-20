package juuxel.adorn.platform

import juuxel.adorn.api.block.BlockVariant
import juuxel.adorn.block.KitchenSinkBlock
import juuxel.adorn.block.SofaBlock

interface BlockFactory {
    fun createSofa(variant: BlockVariant): SofaBlock = SofaBlock(variant)

    fun createSink(variant: BlockVariant): KitchenSinkBlock = KitchenSinkBlock(variant)

    companion object Default : BlockFactory
}
