package juuxel.adorn.compat

import juuxel.adorn.api.block.BlockVariant

interface CompatModule {
    val woodVariants: List<BlockVariant>
    val stoneVariants: List<BlockVariant>
}
