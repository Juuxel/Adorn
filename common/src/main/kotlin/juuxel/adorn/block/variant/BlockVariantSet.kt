package juuxel.adorn.block.variant

interface BlockVariantSet {
    val woodVariants: List<BlockVariant> get() = emptyList()
    val stoneVariants: List<BlockVariant> get() = emptyList()

    fun addVariants(consumer: CustomVariantConsumer) {
        // No customised variants by default.
    }

    fun sortVariants(sorter: Sorter) {
        // No custom sorting by default.
    }

    fun interface CustomVariantConsumer {
        fun add(variant: BlockVariant, kinds: List<BlockKind>)
    }

    fun interface Sorter {
        fun moveAfter(variant: BlockVariant, after: BlockVariant)
    }
}
