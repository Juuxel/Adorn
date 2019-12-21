package juuxel.adorn.json.kitchen

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.AbstractContentGenerator
import io.github.cottonmc.jsonfactory.gens.ResourceRoot
import io.github.cottonmc.jsonfactory.output.suffixed
import juuxel.adorn.json.AdornPlugin
import juuxel.adorn.json.output.MapOutput

object KitchenSinkRecipe : AbstractContentGenerator("kitchen_sink.recipe", "recipes",
    AdornPlugin.KITCHEN, resourceRoot = ResourceRoot.Data) {
    override fun generate(id: Identifier) = listOf(
        MapOutput(
            "type" to "crafting_shapeless",
            "group" to "adorn:kitchen_sink",
            "ingredients" to listOf(
                mapOf(
                    "item" to id.suffixPath("_kitchen_counter")
                ),
                mapOf(
                    "item" to "minecraft:bucket"
                )
            ),
            "result" to mapOf(
                "item" to id.suffixPath("_kitchen_sink"),
                "count" to 1
            )
        ).suffixed("kitchen_sink")
    )
}
