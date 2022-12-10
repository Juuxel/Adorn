package juuxel.adorn.compat

import juuxel.adorn.api.block.BlockVariant
import juuxel.adorn.block.BlockVariantSet

object BlockusCompat : BlockVariantSet {
    override val woodVariants = listOf(BlockVariant.Wood("blockus/white_oak"))
}
