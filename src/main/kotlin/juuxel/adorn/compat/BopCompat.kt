package juuxel.adorn.compat

import juuxel.adorn.Adorn
import juuxel.adorn.api.block.AdornBlockBuilder
import juuxel.adorn.api.block.BlockVariant
import net.minecraftforge.eventbus.api.IEventBus

object BopCompat {
    fun init(modBus: IEventBus) {
        val woodTypes = sequenceOf(
            "fir",
            "redwood",
            "cherry",
            "mahogany",
            "jacaranda",
            "palm",
            "willow",
            "dead",
            "magic",
            "umbran",
            "hellbark",
        ).map { BlockVariant.Wood("biomesoplenty/$it") }

        for (wood in woodTypes) {
            AdornBlockBuilder.create(wood)
                .withEverything()
                .register(Adorn.NAMESPACE, modBus)
        }

        val stoneTypes = sequenceOf(
            "black_sandstone",
            "mud_brick",
            "orange_sandstone",
            "white_sandstone",
        ).map { BlockVariant.Stone("biomesoplenty/$it") }

        for (stone in stoneTypes) {
            AdornBlockBuilder.create(stone)
                .withPlatform()
                .withPost()
                .withStep()
                .register(Adorn.NAMESPACE, modBus)
        }
    }
}
