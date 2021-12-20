package juuxel.adorn.platform.forge

import juuxel.adorn.api.block.BlockVariant
import juuxel.adorn.block.KitchenSinkBlock
import juuxel.adorn.block.SofaBlock
import juuxel.adorn.platform.BlockFactory
import juuxel.adorn.platform.forge.block.KitchenSinkBlockForge
import juuxel.adorn.platform.forge.block.SofaBlockForge

object BlockFactoryImpl : BlockFactory {
    override fun createSofa(variant: BlockVariant): SofaBlock =
        SofaBlockForge(variant)

    override fun createSink(variant: BlockVariant): KitchenSinkBlock =
        KitchenSinkBlockForge(variant)
}
