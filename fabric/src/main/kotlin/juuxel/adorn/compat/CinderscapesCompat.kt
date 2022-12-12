package juuxel.adorn.compat

import juuxel.adorn.block.variant.BlockVariant
import juuxel.adorn.block.variant.BlockVariantSet

object CinderscapesCompat : BlockVariantSet {
    override val woodVariants = listOf(
        "scorched",
        "umbral",
    ).map { BlockVariant.Wood("cinderscapes/$it") }
}
