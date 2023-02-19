package juuxel.adorn.compat

import juuxel.adorn.block.variant.BlockVariant
import juuxel.adorn.block.variant.BlockVariantSet

object PromenadeCompat : BlockVariantSet {
    override val woodVariants = listOf(
        "dark_amaranth",
        "palm",
        "sakura",
    ).map { BlockVariant.Wood("promenade/$it") }
}
