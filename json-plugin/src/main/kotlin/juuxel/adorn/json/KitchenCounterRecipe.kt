package juuxel.adorn.json

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.ContentGenerator
import io.github.cottonmc.jsonfactory.output.suffixed
import juuxel.adorn.json.output.MapOutput

object KitchenCounterRecipe : ContentGenerator("Kitchen Counter Recipe", "recipes",
    AdornPlugin.KITCHEN, resourceRoot = ResourceRoot.Data) {
    override fun generate(id: Identifier) = listOf(
        MapOutput(
            mapOf(
                "type" to "crafting_shaped",
                "group" to "adorn:kitchen_counter",
                "pattern" to listOf("SS", "PP"),
                "key" to mapOf(
                    "S" to mapOf("item" to "minecraft:polished_andesite_slab"),
                    "P" to mapOf("item" to "minecraft:${id.path}_planks")
                ),
                "result" to mapOf(
                    "item" to id.suffixPath("_kitchen_counter"),
                    "count" to 3
                )
            )
        ).suffixed("kitchen_counter")
    )
}
