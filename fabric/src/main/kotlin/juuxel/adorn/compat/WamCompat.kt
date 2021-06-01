package juuxel.adorn.compat

import juuxel.adorn.AdornCommon
import juuxel.adorn.api.block.AdornBlockBuilder
import juuxel.adorn.api.block.BlockVariant

object WamCompat {
    fun init() {
        AdornBlockBuilder.create(BlockVariant.Wood("woods_and_mires/pine"))
            .withEverything()
            .registerIn(AdornCommon.NAMESPACE)
    }
}
