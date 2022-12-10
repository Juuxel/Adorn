package juuxel.adorn.compat

import juuxel.adorn.api.block.BlockVariant
import juuxel.adorn.block.BlockVariantSet

object TraverseCompat : BlockVariantSet {
    override val woodVariants = listOf(BlockVariant.Wood("traverse/fir"))
}
