package juuxel.adorn.compat

import juuxel.adorn.block.variant.BlockVariant
import juuxel.adorn.block.variant.BlockVariantSet

object TerrestriaCompat : BlockVariantSet {
    override val woodVariants = listOf(
        "cypress",
        "hemlock",
        "japanese_maple",
        "rainbow_eucalyptus",
        "redwood",
        "rubber",
        "sakura",
        "yucca_palm",
        "willow",
    ).map { BlockVariant.Wood("terrestria/$it") }

    override val stoneVariants = listOf(
        "basalt",
        "basalt_cobblestone",
        "smooth_basalt",
        "basalt_brick",
        "mossy_basalt_cobblestone",
        "mossy_basalt_brick",
    ).map { BlockVariant.Stone("terrestria/$it") }
}
