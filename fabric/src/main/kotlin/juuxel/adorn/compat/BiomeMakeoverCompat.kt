package juuxel.adorn.compat

import juuxel.adorn.api.block.BlockVariant

object BiomeMakeoverCompat : BlockVariantSet {
    override val woodVariants = listOf(
        "ancient_oak",
        "blighted_balsa",
        "willow",
        "swamp_cypress",
    ).map { BlockVariant.Wood("biomemakeover/$it") }
}
