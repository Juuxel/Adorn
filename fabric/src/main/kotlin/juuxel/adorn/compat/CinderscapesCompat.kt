package juuxel.adorn.compat

import juuxel.adorn.AdornCommon
import juuxel.adorn.api.block.AdornBlockBuilder
import juuxel.adorn.api.block.BlockVariant

object CinderscapesCompat {
    fun init() {
        val fungusTypes = sequenceOf(
            "scorched",
            "umbral",
        ).map { BlockVariant.Wood("cinderscapes/$it") }

        for (fungus in fungusTypes) {
            AdornBlockBuilder.create(fungus)
                .withEverything()
                .registerIn(AdornCommon.NAMESPACE)
        }
    }
}
