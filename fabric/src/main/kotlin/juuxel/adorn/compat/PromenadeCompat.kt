package juuxel.adorn.compat

import juuxel.adorn.block.variant.BlockVariant
import juuxel.adorn.block.variant.BlockVariantSet

object PromenadeCompat : BlockVariantSet {
    override val woodVariants = listOf(
        "cherry_oak",
        "dark_amaranth",
        "palm",
    ).map { BlockVariant.Wood("promenade/$it") }
}
