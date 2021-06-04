package juuxel.adorn.compat

import juuxel.adorn.AdornCommon
import juuxel.adorn.api.block.AdornBlockBuilder
import juuxel.adorn.api.block.BlockVariant

object TraverseCompat {
    fun init() {
        val fir = BlockVariant.Wood("traverse/fir")
        AdornBlockBuilder.create(fir)
            .withEverything()
            .registerIn(AdornCommon.NAMESPACE)
    }
}
