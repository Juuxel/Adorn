package juuxel.adorn.compat

import juuxel.adorn.api.block.BlockVariant

object PromenadeCompat : BlockVariantSet {
    override val woodVariants = listOf(
        "cherry_oak",
        "dark_amaranth",
        "palm",
    ).map { BlockVariant.Wood("promenade/$it") }
}
