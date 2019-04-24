package juuxel.adorn.json

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.ContentGenerator
import io.github.cottonmc.jsonfactory.output.suffixed
import juuxel.adorn.json.output.MapOutput

object KitchenCupboardRecipe : ContentGenerator("Kitchen Cupboard Recipe", "recipes", AdornPlugin.KITCHEN, resourceRoot = ResourceRoot.Data) {
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
