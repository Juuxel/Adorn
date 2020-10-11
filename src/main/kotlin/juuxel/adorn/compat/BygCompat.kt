package juuxel.adorn.compat

import juuxel.adorn.Adorn
import juuxel.adorn.api.block.AdornBlockBuilder
import juuxel.adorn.api.block.BlockVariant

object BygCompat {
    fun init() {
        val woodTypes = sequenceOf(
            "aspen",
            "baobab",
            "blue_enchanted",
            "cherry",
            "cika",
            "cypress",
            "ebony",
            "fir",
            "green_enchanted",
            "holly",
            "jacaranda",
            "mahogany",
            "mangrove",
            "maple",
            "pine",
            "rainbow_eucalyptus",
            "redwood",
            "skyris",
            "willow",
            "witch_hazel",
            "zelkova"
        ).map { BlockVariant.Wood("byg/$it") }

        for (wood in woodTypes) {
            AdornBlockBuilder.create(wood)
                .withEverything()
                .registerIn(Adorn.NAMESPACE)
        }

        val stoneTypes = sequenceOf(
            "dacite_cobblestone",
            "mossy_stone",
            "rocky_stone",
            "scoria_stone",
            "scoria_cobblestone",
            "scoria_stonebrick",
            "soapstone",
            "polished_soapstone",
            "soapstone_brick",
            "soapstone_tile",
            "red_rock",
            "red_rock_brick",
            "mossy_red_rock_brick",
            "cracked_red_rock_brick",
            "chiseled_red_rock_brick"
        ).map { BlockVariant.Stone("byg/$it") }

        for (stone in stoneTypes) {
            AdornBlockBuilder.create(stone)
                .withPlatform()
                .withPost()
                .withStep()
                .registerIn(Adorn.NAMESPACE)
        }
    }
}
