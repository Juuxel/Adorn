package juuxel.adorn.platform.forge.compat

import juuxel.adorn.block.variant.BlockVariant
import juuxel.adorn.block.variant.BlockVariantSet

object BiomesOPlentyCompat : BlockVariantSet {
    override val woodVariants = listOf(
        "cherry",
        "dead",
        "fir",
        "hellbark",
        "jacaranda",
        "magic",
        "mahogany",
        "palm",
        "redwood",
        "umbran",
        "willow",
    ).map { BlockVariant.Wood("biomesoplenty/$it") }

    override val stoneVariants = listOf(
        "black_sandstone",
        "cut_black_sandstone",
        "smooth_black_sandstone",
        "orange_sandstone",
        "cut_orange_sandstone",
        "smooth_orange_sandstone",
        "white_sandstone",
        "cut_white_sandstone",
        "smooth_white_sandstone",
    ).map { BlockVariant.Stone("biomesoplenty/$it") }
}
