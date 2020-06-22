package juuxel.adorn.compat.woodsandmires

import juuxel.adorn.Adorn
import juuxel.adorn.api.block.AdornBlockBuilder
import juuxel.adorn.api.block.BlockVariant

object AdornWamCompat {
    fun init() {
        AdornBlockBuilder.create(BlockVariant.Wood("woods_and_mires/pine"))
            .withEverything()
            .registerIn(Adorn.NAMESPACE)
    }
}
