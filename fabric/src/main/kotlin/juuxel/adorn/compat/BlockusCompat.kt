package juuxel.adorn.compat

import juuxel.adorn.block.variant.BlockVariant
import juuxel.adorn.block.variant.BlockVariantSet

object BlockusCompat : BlockVariantSet {
    override val woodVariants = listOf(BlockVariant.Wood("blockus/white_oak"))
}
