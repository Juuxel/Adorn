package juuxel.adorn.compat

import juuxel.adorn.Adorn
import juuxel.adorn.api.block.AdornBlockBuilder
import juuxel.adorn.api.block.BlockVariant

object TraverseCompat {
    fun init() {
        val fir = BlockVariant.Wood("traverse_fir")
        AdornBlockBuilder.create(fir)
            .withEverything()
            .registerIn(Adorn.NAMESPACE)
    }
}
