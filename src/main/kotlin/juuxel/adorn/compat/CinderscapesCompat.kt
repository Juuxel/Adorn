package juuxel.adorn.compat

import juuxel.adorn.Adorn
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
                .registerIn(Adorn.NAMESPACE)
        }
    }
}
