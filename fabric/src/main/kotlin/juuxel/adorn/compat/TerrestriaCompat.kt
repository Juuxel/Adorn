package juuxel.adorn.compat

import juuxel.adorn.AdornCommon
import juuxel.adorn.api.block.AdornBlockBuilder
import juuxel.adorn.api.block.BlockVariant

object TerrestriaCompat {
    fun init() {
        val woodTypes = sequenceOf(
            "cypress",
            "hemlock",
            "japanese_maple",
            "rainbow_eucalyptus",
            "redwood",
            "rubber",
            "sakura",
            "yucca_palm",
            "willow",
        ).map { BlockVariant.Wood("terrestria/$it") }

        for (wood in woodTypes) {
            AdornBlockBuilder.create(wood)
                .withEverything()
                .registerIn(AdornCommon.NAMESPACE)
        }

        val stoneTypes = sequenceOf(
            "basalt",
            "basalt_cobblestone",
            "smooth_basalt",
            "basalt_brick",
            "mossy_basalt_cobblestone",
            "mossy_basalt_brick",
        ).map { BlockVariant.Stone("terrestria/$it") }

        for (stone in stoneTypes) {
            AdornBlockBuilder.create(stone)
                .withPlatform()
                .withPost()
                .withStep()
                .registerIn(AdornCommon.NAMESPACE)
        }
    }
}
