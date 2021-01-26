package juuxel.adorn.compat

import juuxel.adorn.Adorn
import juuxel.adorn.api.block.AdornBlockBuilder
import juuxel.adorn.api.block.BlockVariant
import net.minecraftforge.eventbus.api.IEventBus

object BygCompat {
    fun init(modBus: IEventBus) {
        val woodTypes = sequenceOf(
            "aspen",
            "baobab",
            "blue_enchanted",
            "bulbis",
            "cherry",
            "cika",
            "cypress",
            "ebony",
            "embur",
            "ether",
            "fir",
            "green_enchanted",
            "holly",
            "jacaranda",
            "lament",
            "mahogany",
            "mangrove",
            "maple",
            "nightshade",
            "palm",
            "pine",
            "rainbow_eucalyptus",
            "redwood",
            "skyris",
            "sythian",
            "willow",
            "witch_hazel",
            "zelkova"
        ).map { BlockVariant.Wood("byg/$it") }

        for (wood in woodTypes) {
            AdornBlockBuilder.create(wood)
                .withEverything()
                .register(Adorn.NAMESPACE, modBus)
        }

        val stoneTypes = sequenceOf(
            "dacite",
            "dacite_brick",
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
                .register(Adorn.NAMESPACE, modBus)
        }
    }
}
