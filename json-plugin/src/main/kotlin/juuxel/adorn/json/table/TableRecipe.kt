package juuxel.adorn.json.table

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.AbstractContentGenerator
import io.github.cottonmc.jsonfactory.gens.ResourceRoot
import io.github.cottonmc.jsonfactory.output.suffixed
import juuxel.adorn.json.AdornPlugin
import juuxel.adorn.json.output.MapOutput

object TableRecipe : AbstractContentGenerator("table.recipe", "recipes",
    AdornPlugin.TABLE, resourceRoot = ResourceRoot.Data) {
    override fun generate(id: Identifier) = listOf(
        MapOutput(
            mapOf(
                "type" to "crafting_shaped",
                "group" to "adorn:table",
                "pattern" to listOf("SSS", "# #", "# #"),
                "key" to mapOf(
                    "S" to mapOf("item" to "minecraft:${id.path}_slab"),
                    "#" to mapOf("item" to "minecraft:stick")
                ),
                "result" to mapOf(
                    "item" to id.suffixPath("_table"),
                    "count" to 3
                )
            )
        ).suffixed("table")
    )
}
