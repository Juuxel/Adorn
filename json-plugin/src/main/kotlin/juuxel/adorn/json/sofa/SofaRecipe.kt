package juuxel.adorn.json.sofa

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.AbstractContentGenerator
import io.github.cottonmc.jsonfactory.gens.ResourceRoot
import io.github.cottonmc.jsonfactory.output.suffixed
import juuxel.adorn.json.AdornPlugin
import juuxel.adorn.json.output.MapOutput

object SofaRecipe : AbstractContentGenerator("sofa.recipe", "recipes", AdornPlugin.SOFA, resourceRoot = ResourceRoot.Data) {
    override fun generate(id: Identifier) = listOf(
        MapOutput(
            mapOf(
                "type" to "crafting_shaped",
                "group" to "adorn:sofa",
                "pattern" to listOf(" W", "WW", "SS"),
                "key" to mapOf(
                    "S" to mapOf("tag" to "forge:rods/wooden"),
                    "W" to mapOf("item" to "minecraft:${id.path}_wool")
                ),
                "result" to mapOf(
                    "item" to id.suffixPath("_sofa"),
                    "count" to 3
                )
            )
        ).suffixed("sofa")
    )
}
