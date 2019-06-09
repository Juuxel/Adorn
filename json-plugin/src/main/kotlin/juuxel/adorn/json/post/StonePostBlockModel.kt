package juuxel.adorn.json.post

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.AbstractContentGenerator
import io.github.cottonmc.jsonfactory.output.model.Model
import io.github.cottonmc.jsonfactory.output.suffixed
import juuxel.adorn.json.AdornPlugin

object StonePostBlockModel : AbstractContentGenerator("post.stone.block_model", "models/block", AdornPlugin.POST) {
    override fun generate(id: Identifier) = listOf(
        Model(
            parent = Identifier("adorn", "block/templates/post"),
            textures = mapOf(
                "texture" to Identifier.mc("block/${id.path}")
            )
        ).suffixed("post")
    )
}
