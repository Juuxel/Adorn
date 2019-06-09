package juuxel.adorn.json.kitchen

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.AbstractContentGenerator
import io.github.cottonmc.jsonfactory.gens.ResourceRoot
import io.github.cottonmc.jsonfactory.output.suffixed
import juuxel.adorn.json.AdornPlugin
import juuxel.adorn.json.output.MapOutput

object KitchenCupboardRecipe : AbstractContentGenerator("kitchen_cupboard.recipe", "recipes",
    AdornPlugin.KITCHEN, resourceRoot = ResourceRoot.Data) {
    override fun generate(id: Identifier) = listOf(
        MapOutput(
            mapOf(
                "type" to "crafting_shapeless",
                "group" to "adorn:kitchen_cupboard",
                "ingredients" to listOf(
                    mapOf("item" to "minecraft:chest"),
                    mapOf("item" to id.suffixPath("_kitchen_counter")),
                    mapOf("item" to id.suffixPath("_kitchen_counter"))
                ),
                "result" to mapOf(
                    "item" to id.suffixPath("_kitchen_cupboard"),
                    "count" to 2
                )
            )
        ).suffixed("kitchen_cupboard")
    )
}
