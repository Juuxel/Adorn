package juuxel.adorn.compat

import juuxel.adorn.Adorn
import juuxel.adorn.api.block.AdornBlockBuilder
import juuxel.adorn.api.block.BlockVariant
import net.minecraftforge.eventbus.api.IEventBus

object TraverseCompat {
    fun init(modBus: IEventBus) {
        val woodTypes = sequenceOf(
            "fir",
        ).map { BlockVariant.Wood("traverse/$it") }

        for (wood in woodTypes) {
            AdornBlockBuilder.create(wood)
                .withEverything()
                .register(Adorn.NAMESPACE, modBus)
        }
    }
}
