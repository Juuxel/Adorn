package juuxel.adorn.json.table

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.AbstractContentGenerator
import io.github.cottonmc.jsonfactory.gens.ResourceRoot
import io.github.cottonmc.jsonfactory.output.suffixed
import juuxel.adorn.json.AdornPlugin
import juuxel.adorn.json.output.MapOutput

object CoffeeTableRecipe : AbstractContentGenerator("coffee_table.recipe", "recipes",
    AdornPlugin.TABLE, resourceRoot = ResourceRoot.Data) {
    override fun generate(id: Identifier) = listOf(
        MapOutput(
            mapOf(
                "type" to "crafting_shaped",
                "group" to "adorn:coffee_table",
                "pattern" to listOf("S-S", "# #"),
                "key" to mapOf(
                    "S" to mapOf("item" to "minecraft:${id.path}_slab"),
                    "#" to mapOf("item" to "minecraft:stick"),
                    "-" to mapOf("tag" to "forge:glass_panes")
                ),
                "result" to mapOf(
                    "item" to id.suffixPath("_coffee_table"),
                    "count" to 2
                )
            )
        ).suffixed("coffee_table")
    )
}
