package juuxel.adorn.json.post

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.ContentGenerator
import io.github.cottonmc.jsonfactory.output.suffixed
import juuxel.adorn.json.AdornPlugin
import juuxel.adorn.json.output.MapOutput

object StonePostRecipe : ContentGenerator("Stone Post Recipe", "recipes", AdornPlugin.POST, resourceRoot = ResourceRoot.Data) {
    override fun generate(id: Identifier) = listOf(
        MapOutput(
            mapOf(
                "type" to "crafting_shaped",
                "group" to "adorn:post",
                "pattern" to listOf("R", "R", "S"),
                "key" to mapOf(
                    "S" to mapOf("item" to "minecraft:${id.path}"),
                    "R" to mapOf("item" to "c:stone_rod")
                ),
                "result" to mapOf(
                    "item" to id.suffixPath("_post"),
                    "count" to 4
                )
            )
        ).suffixed("post")
    )
}
