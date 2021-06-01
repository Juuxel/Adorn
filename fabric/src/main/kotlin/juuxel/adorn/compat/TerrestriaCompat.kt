package juuxel.adorn.compat

import juuxel.adorn.AdornCommon
import juuxel.adorn.api.block.AdornBlockBuilder
import juuxel.adorn.api.block.BlockVariant

object TerrestriaCompat {
    fun init() {
        // TODO: Get rid of this in 1.17
        val oldWoodTypesOldBlocks = sequenceOf(
            "cypress", "hemlock", "japanese_maple", "rainbow_eucalyptus", "redwood",
            "rubber", "sakura", "willow"
        ).map { BlockVariant.Wood("terrestria_$it") }

        for (wood in oldWoodTypesOldBlocks) {
            AdornBlockBuilder.create(wood)
                .withPost()
                .withPlatform()
                .withStep()
                .withDrawer()
                .withChair()
                .withTable()
                .withKitchenBlocks()
                .withShelf()
                .withCoffeeTable()
                .registerIn(AdornCommon.NAMESPACE)
        }

        val oldWoodTypesNewBlocks = sequenceOf(
            "cypress", "hemlock", "japanese_maple", "rainbow_eucalyptus", "redwood",
            "rubber", "sakura", "willow"
        ).map { BlockVariant.Wood("terrestria/$it") }

        for (wood in oldWoodTypesNewBlocks) {
            AdornBlockBuilder.create(wood)
                .withBench()
                .registerIn(AdornCommon.NAMESPACE)
        }

        val newWoodTypes = sequenceOf(
            "yucca_palm"
        ).map { BlockVariant.Wood("terrestria/$it") }

        for (wood in newWoodTypes) {
            AdornBlockBuilder.create(wood)
                .withEverything()
                .registerIn(AdornCommon.NAMESPACE)
        }

        val oldStoneTypes = sequenceOf(
            "basalt", "basalt_cobblestone"
        ).map { BlockVariant.Stone("terrestria_$it") }

        val newStoneTypes = sequenceOf(
            "smooth_basalt",
            "basalt_brick",
            "mossy_basalt_cobblestone",
            "mossy_basalt_brick",
        ).map { BlockVariant.Stone("terrestria/$it") }

        val stoneTypes = oldStoneTypes + newStoneTypes

        for (stone in stoneTypes) {
            AdornBlockBuilder.create(stone)
                .withPlatform()
                .withPost()
                .withStep()
                .registerIn(AdornCommon.NAMESPACE)
        }
    }
}
