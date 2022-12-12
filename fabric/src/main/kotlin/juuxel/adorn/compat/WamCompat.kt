package juuxel.adorn.compat

import juuxel.adorn.block.variant.BlockVariant
import juuxel.adorn.block.variant.BlockVariantSet

object WamCompat : BlockVariantSet {
    override val woodVariants = listOf(BlockVariant.Wood("woods_and_mires/pine"))
}
