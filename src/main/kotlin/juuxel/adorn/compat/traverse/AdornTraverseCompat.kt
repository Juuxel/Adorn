package juuxel.adorn.compat.traverse

import juuxel.adorn.Adorn
import juuxel.adorn.api.block.AdornBlockBuilder
import juuxel.adorn.api.util.BlockVariant

object AdornTraverseCompat {
    fun init() {
        val fir = BlockVariant.Wood("traverse_fir")
        AdornBlockBuilder.create(fir)
            .withChair()
            .withDrawer()
            .withKitchenCounter()
            .withKitchenCupboard()
            .withPlatform()
            .withPost()
            .withShelf()
            .withStep()
            .withTable()
            .registerIn(Adorn.NAMESPACE)
    }
}
