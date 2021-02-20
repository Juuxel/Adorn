package juuxel.adorn.compat

import juuxel.adorn.Adorn
import juuxel.adorn.api.block.AdornBlockBuilder
import juuxel.adorn.api.block.BlockVariant

object TraverseCompat {
    fun init() {
        // TODO: Get rid of this in 1.17
        val oldFir = BlockVariant.Wood("traverse_fir")
        AdornBlockBuilder.create(oldFir)
            .withPost()
            .withPlatform()
            .withStep()
            .withDrawer()
            .withChair()
            .withTable()
            .withKitchenBlocks()
            .withShelf()
            .withCoffeeTable()
            .registerIn(Adorn.NAMESPACE)

        val newFir = BlockVariant.Wood("traverse/fir")
        AdornBlockBuilder.create(oldFir)
            .withBench()
            .registerIn(Adorn.NAMESPACE)
    }
}
