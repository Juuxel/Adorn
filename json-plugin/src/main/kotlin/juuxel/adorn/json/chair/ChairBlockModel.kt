package juuxel.adorn.json.chair

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.AbstractContentGenerator
import io.github.cottonmc.jsonfactory.output.model.Model
import io.github.cottonmc.jsonfactory.output.suffixed
import juuxel.adorn.json.AdornPlugin

internal object ChairBlockModel : AbstractContentGenerator("chair.block_model", "models/block",
    AdornPlugin.CHAIR
) {
    override fun generate(id: Identifier) = listOf(
        Model(
            parent = Identifier("adorn", "block/templates/chair_upper"),
            textures = mapOf(
                "planks" to Identifier.mc("block/${id.path}_planks")
            )
        ).suffixed("chair_upper"),
        Model(
            parent = Identifier("adorn", "block/templates/chair_lower"),
            textures = mapOf(
                "planks" to Identifier.mc("block/${id.path}_planks")
            )
        ).suffixed("chair_lower")
    )
}
