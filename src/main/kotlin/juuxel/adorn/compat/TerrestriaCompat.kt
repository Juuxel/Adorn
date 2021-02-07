package juuxel.adorn.compat

import juuxel.adorn.Adorn
import juuxel.adorn.api.block.AdornBlockBuilder
import juuxel.adorn.api.block.BlockVariant

object TerrestriaCompat {
    fun init() {
        val oldWoodTypes = sequenceOf(
            "cypress", "hemlock", "japanese_maple", "rainbow_eucalyptus", "redwood",
            "rubber", "sakura", "willow"
        ).map { BlockVariant.Wood("terrestria_$it") }

        val newWoodTypes = sequenceOf(
            "yucca_palm"
        ).map { BlockVariant.Wood("terrestria/$it") }

        val woodTypes = oldWoodTypes + newWoodTypes

        val oldStoneTypes = sequenceOf(
            "basalt", "basalt_cobblestone"
        ).map { BlockVariant.Stone("terrestria_$it") }

        val newStoneTypes = sequenceOf(
            "smooth_basalt",
            "basalt_brick",
            "mossy_basalt",
            "mossy_basalt_brick",
        ).map { BlockVariant.Stone("terrestria/$it") }

        val stoneTypes = oldStoneTypes + newStoneTypes

        for (wood in woodTypes) {
            AdornBlockBuilder.create(wood)
                .withEverything()
                .registerIn(Adorn.NAMESPACE)
        }

        for (stone in stoneTypes) {
            AdornBlockBuilder.create(stone)
                .withPlatform()
                .withPost()
                .withStep()
                .registerIn(Adorn.NAMESPACE)
        }
    }
}
