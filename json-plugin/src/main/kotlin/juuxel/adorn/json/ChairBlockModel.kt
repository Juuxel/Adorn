package juuxel.adorn.json

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.ContentGenerator
import io.github.cottonmc.jsonfactory.output.model.Model
import io.github.cottonmc.jsonfactory.output.suffixed

internal object ChairBlockModel : ContentGenerator("Chair Block Model", "models/block",
    AdornPlugin.CHAIR
) {
    override fun generate(id: Identifier) = listOf(
        Model(
            parent = Identifier("adorn", "block/templates/chair"),
            textures = mapOf(
                "planks" to Identifier.mc("block/${id.path}_planks")
            )
        ).suffixed("chair")
    )
}
