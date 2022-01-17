package juuxel.adorn.compat

import juuxel.adorn.api.block.BlockVariant

interface BlockVariantSet {
    val woodVariants: List<BlockVariant> get() = emptyList()
    val stoneVariants: List<BlockVariant> get() = emptyList()
}
