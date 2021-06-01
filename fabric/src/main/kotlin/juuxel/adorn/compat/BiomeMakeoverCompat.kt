package juuxel.adorn.compat

import juuxel.adorn.AdornCommon
import juuxel.adorn.api.block.AdornBlockBuilder
import juuxel.adorn.api.block.BlockVariant

object BiomeMakeoverCompat {
    fun init() {
        val woodTypes = sequenceOf(
            "blighted_balsa",
            "willow",
            "swamp_cypress",
        ).map { BlockVariant.Wood("biomemakeover/$it") }

        for (wood in woodTypes) {
            AdornBlockBuilder.create(wood)
                .withEverything()
                .registerIn(AdornCommon.NAMESPACE)
        }
    }
}
