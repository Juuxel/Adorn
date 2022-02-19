package juuxel.adorn.platform.forge.compat

import juuxel.adorn.api.block.BlockVariant
import juuxel.adorn.compat.BlockVariantSet

object ArchitectsPaletteCompat : BlockVariantSet {
    override val woodVariants = listOf(BlockVariant.Wood("architects_palette/twisted"))

    override val stoneVariants = listOf(
        "myonite",
        "myonite_brick",
        "mushy_myonite_brick",
    ).map { BlockVariant.Stone("architects_palette/$it") }
}
