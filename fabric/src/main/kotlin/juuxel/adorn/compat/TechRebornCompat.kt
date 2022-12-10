package juuxel.adorn.compat

import juuxel.adorn.api.block.BlockVariant
import juuxel.adorn.block.BlockVariantSet

object TechRebornCompat : BlockVariantSet {
    override val woodVariants = listOf(BlockVariant.Wood("techreborn/rubber"))
}
