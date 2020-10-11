package juuxel.adorn.compat

import juuxel.adorn.Adorn
import juuxel.adorn.api.block.AdornBlockBuilder
import juuxel.adorn.api.block.BlockVariant

object WamCompat {
    fun init() {
        AdornBlockBuilder.create(BlockVariant.Wood("woods_and_mires/pine"))
            .withEverything()
            .registerIn(Adorn.NAMESPACE)
    }
}
