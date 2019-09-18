package juuxel.adorn.compat.terrestria

import juuxel.adorn.Adorn
import juuxel.adorn.api.block.AdornBlockBuilder
import juuxel.adorn.api.block.BlockVariant

object AdornTerrestriaCompat {
    fun init() {
        val woodTypes = sequenceOf(
            "cypress", "hemlock", "japanese_maple", "rainbow_eucalyptus", "redwood",
            "rubber", "sakura", "willow"
        ).map { BlockVariant.Wood("terrestria_$it") }

        val stoneTypes = sequenceOf(
            "basalt", "basalt_cobblestone"
        ).map { BlockVariant.Stone("terrestria_$it") }

        for (wood in woodTypes) {
            AdornBlockBuilder.create(wood)
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

        for (stone in stoneTypes) {
            AdornBlockBuilder.create(stone)
                .withPlatform()
                .withPost()
                .withStep()
                .registerIn(Adorn.NAMESPACE)
        }
    }
}
