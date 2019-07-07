package juuxel.adorn.json.shelf

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.AbstractContentGenerator
import io.github.cottonmc.jsonfactory.output.model.Model
import io.github.cottonmc.jsonfactory.output.suffixed
import juuxel.adorn.json.AdornPlugin

object ShelfBlockModel : AbstractContentGenerator("shelf.block_model", "models/block", AdornPlugin.SHELF) {
    override fun generate(id: Identifier) = listOf(
        Model(
            parent = Identifier("adorn", "block/templates/shelf"),
            textures = mapOf(
                "texture" to Identifier.mc("block/${id.path}_planks")
            )
        ).suffixed("shelf")
    )
}
