package juuxel.adorn.json.drawer

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.AbstractContentGenerator
import io.github.cottonmc.jsonfactory.gens.ResourceRoot
import io.github.cottonmc.jsonfactory.output.suffixed
import juuxel.adorn.json.AdornPlugin
import juuxel.adorn.json.output.MapOutput

object DrawerRecipe : AbstractContentGenerator("drawer.recipe", "recipes",
    AdornPlugin.DRAWER, resourceRoot = ResourceRoot.Data) {
    override fun generate(id: Identifier) = listOf(
        MapOutput(
            mapOf(
                "type" to "crafting_shaped",
                "group" to "adorn:drawer",
                "pattern" to listOf("S", "C", "S"),
                "key" to mapOf(
                    "C" to mapOf("item" to "minecraft:chest"),
                    "S" to mapOf("item" to "minecraft:${id.path}_slab")
                ),
                "result" to mapOf(
                    "item" to id.suffixPath("_drawer"),
                    "count" to 2
                )
            )
        ).suffixed("drawer")
    )
}
