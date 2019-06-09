package juuxel.adorn.json.platform

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.AbstractContentGenerator
import io.github.cottonmc.jsonfactory.gens.ResourceRoot
import io.github.cottonmc.jsonfactory.output.suffixed
import juuxel.adorn.json.AdornPlugin
import juuxel.adorn.json.output.MapOutput

object PlatformRecipe : AbstractContentGenerator("platform.recipe", "recipes", AdornPlugin.PLATFORM, resourceRoot = ResourceRoot.Data) {
    override fun generate(id: Identifier) = listOf(
        MapOutput(
            mapOf(
                "type" to "crafting_shaped",
                "group" to "adorn:platform",
                "pattern" to listOf("S", "P"),
                "key" to mapOf(
                    "S" to mapOf("item" to "minecraft:${id.path}_slab"),
                    "P" to mapOf("item" to id.suffixPath("_post"))
                ),
                "result" to mapOf(
                    "item" to id.suffixPath("_platform"),
                    "count" to 2
                )
            )
        ).suffixed("platform")
    )
}
