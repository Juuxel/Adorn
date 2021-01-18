package juuxel.adorn.json.step

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.AbstractContentGenerator
import io.github.cottonmc.jsonfactory.gens.ResourceRoot
import io.github.cottonmc.jsonfactory.output.suffixed
import juuxel.adorn.json.AdornPlugin
import juuxel.adorn.json.output.MapOutput

object StoneStepRecipe : AbstractContentGenerator("step.stone.recipe", "recipes", AdornPlugin.STEP, resourceRoot = ResourceRoot.Data) {
    override fun generate(id: Identifier) = listOf(
        MapOutput(
            mapOf(
                "type" to "crafting_shaped",
                "group" to "adorn:step",
                "pattern" to listOf("S", "#"),
                "key" to mapOf(
                    "S" to mapOf("item" to "minecraft:${id.path}_slab"),
                    "#" to mapOf("tag" to "forge:rods/stone")
                ),
                "result" to mapOf(
                    "item" to id.suffixPath("_step"),
                    "count" to 1
                )
            )
        ).suffixed("step")
    )
}
