package juuxel.adorn.compat

import juuxel.adorn.api.block.BlockVariant
import juuxel.adorn.block.BlockVariantSet

object WamCompat : BlockVariantSet {
    override val woodVariants = listOf(BlockVariant.Wood("woods_and_mires/pine"))
}
