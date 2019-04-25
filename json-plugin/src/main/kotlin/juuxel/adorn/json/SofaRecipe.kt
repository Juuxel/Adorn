package juuxel.adorn.json

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.ContentGenerator
import io.github.cottonmc.jsonfactory.output.suffixed
import juuxel.adorn.json.output.MapOutput

object SofaRecipe : ContentGenerator("Sofa Recipe", "recipes", AdornPlugin.SOFA, resourceRoot = ResourceRoot.Data) {
    override fun generate(id: Identifier) = listOf(
        MapOutput(
            mapOf(
                "type" to "crafting_shaped",
                "group" to "adorn:sofa",
                "pattern" to listOf(" W", "WW", "SS"),
                "key" to mapOf(
                    "S" to mapOf("item" to "minecraft:stick"),
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
