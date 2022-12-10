package juuxel.adorn.block

import juuxel.adorn.api.block.BlockVariant

interface BlockVariantSet {
    val woodVariants: List<BlockVariant> get() = emptyList()
    val stoneVariants: List<BlockVariant> get() = emptyList()

    fun addVariants(consumer: (BlockVariant, List<BlockKind>) -> Unit) {
        // No customised variants by default.
    }
}
