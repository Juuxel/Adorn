package juuxel.adorn.json.shelf

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.AbstractContentGenerator
import io.github.cottonmc.jsonfactory.gens.ResourceRoot
import io.github.cottonmc.jsonfactory.output.suffixed
import juuxel.adorn.json.AdornPlugin
import juuxel.adorn.json.output.MapOutput

object ShelfRecipe : AbstractContentGenerator("shelf.recipe", "recipes",
    AdornPlugin.SHELF, resourceRoot = ResourceRoot.Data) {
    override fun generate(id: Identifier) = listOf(
        MapOutput(
            mapOf(
                "type" to "crafting_shaped",
                "group" to "adorn:shelf",
                "pattern" to listOf("---", "/ /"),
                "key" to mapOf(
                    "-" to mapOf("item" to "minecraft:${id.path}_slab"),
                    "/" to mapOf("item" to "minecraft:stick")
                ),
                "result" to mapOf(
                    "item" to id.suffixPath("_shelf"),
                    "count" to 2
                )
            )
        ).suffixed("shelf")
    )
}
