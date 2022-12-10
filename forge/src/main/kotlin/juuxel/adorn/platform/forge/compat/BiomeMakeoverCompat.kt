package juuxel.adorn.platform.forge.compat

import juuxel.adorn.api.block.BlockVariant
import juuxel.adorn.compat.BlockVariantSet

object BiomeMakeoverCompat : BlockVariantSet {
    override val woodVariants = listOf(
        "ancient_oak",
        "blighted_balsa",
        "willow",
        "swamp_cypress",
    ).map { BlockVariant.Wood("biomemakeover/$it") }
}
