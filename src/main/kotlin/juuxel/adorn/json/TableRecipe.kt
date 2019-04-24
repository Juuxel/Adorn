package juuxel.adorn.json

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.ContentGenerator
import io.github.cottonmc.jsonfactory.output.suffixed
import juuxel.adorn.json.output.MapOutput

object TableRecipe : ContentGenerator("Table Recipe", "recipes", AdornPlugin.TABLE, resourceRoot = ResourceRoot.Data) {
    override fun generate(id: Identifier) = listOf(
        MapOutput(
            mapOf(
                "type" to "crafting_shaped",
                "group" to "adorn:table",
                "pattern" to listOf("SS", "##"),
                "key" to mapOf(
                    "S" to mapOf("item" to "minecraft:${id.path}_slab"),
                    "#" to mapOf("item" to "minecraft:stick")
                ),
                "result" to mapOf(
                    "item" to id.suffixPath("_table"),
                    "count" to 2
                )
            )
        ).suffixed("table")
    )
}
