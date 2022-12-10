package juuxel.adorn.compat

import juuxel.adorn.api.block.BlockVariant
import juuxel.adorn.block.BlockVariantSet

object CinderscapesCompat : BlockVariantSet {
    override val woodVariants = listOf(
        "scorched",
        "umbral",
    ).map { BlockVariant.Wood("cinderscapes/$it") }
}
