package juuxel.adorn.json.lamp

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.AbstractContentGenerator
import io.github.cottonmc.jsonfactory.gens.ResourceRoot
import io.github.cottonmc.jsonfactory.output.suffixed
import juuxel.adorn.json.AdornPlugin
import juuxel.adorn.json.output.MapOutput

object TallLampRecipe : AbstractContentGenerator("tall_lamp.recipe", "recipes",
    AdornPlugin.OTHER, resourceRoot = ResourceRoot.Data) {
    override fun generate(id: Identifier) = listOf(
        MapOutput(
            mapOf(
                "type" to "crafting_shaped",
                "group" to "adorn:tall_lamp",
                "pattern" to listOf("-", "|", "|"),
                "key" to mapOf(
                    "-" to mapOf("item" to "minecraft:${id.path}_carpet"),
                    "|" to mapOf("item" to "adorn:stone_torch")
                ),
                "result" to mapOf(
                    "item" to id.suffixPath("_tall"),
                    "count" to 1
                )
            )
        ).suffixed("tall_lamp")
    )
}
