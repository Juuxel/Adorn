package juuxel.adorn.compat

import juuxel.adorn.api.block.BlockVariant

object CinderscapesCompat : BlockVariantSet {
    override val woodVariants = listOf(
        "scorched",
        "umbral",
    ).map { BlockVariant.Wood("cinderscapes/$it") }
}
