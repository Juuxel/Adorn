package juuxel.adorn.json.post

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.AbstractContentGenerator
import io.github.cottonmc.jsonfactory.gens.ResourceRoot
import io.github.cottonmc.jsonfactory.output.suffixed
import juuxel.adorn.json.AdornPlugin
import juuxel.adorn.json.output.MapOutput

object WoodenPostRecipe : AbstractContentGenerator("post.wooden.recipe", "recipes", AdornPlugin.POST, resourceRoot = ResourceRoot.Data) {
    override fun generate(id: Identifier) = listOf(
        MapOutput(
            mapOf(
                "type" to "crafting_shaped",
                "group" to "adorn:post",
                "pattern" to listOf("S", "S", "P"),
                "key" to mapOf(
                    "P" to mapOf("item" to "minecraft:${id.path}_planks"),
                    "S" to mapOf("item" to "minecraft:stick")
                ),
                "result" to mapOf(
                    "item" to id.suffixPath("_post"),
                    "count" to 4
                )
            )
        ).suffixed("post")
    )
}
