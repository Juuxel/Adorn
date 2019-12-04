package juuxel.adorn.json.crate

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.AbstractContentGenerator
import io.github.cottonmc.jsonfactory.output.model.Model
import io.github.cottonmc.jsonfactory.output.suffixed
import juuxel.adorn.json.AdornPlugin

object CrateBlockModel : AbstractContentGenerator("crate.block_model", "models/block",
    AdornPlugin.OTHER
) {
    override fun generate(id: Identifier) = listOf(
        Model(
            parent = Identifier("adorn", "block/templates/crate"),
            textures = mapOf(
                "side" to id.wrapPath("block/", "_crate")
            )
        ).suffixed("crate")
    )
}
