package juuxel.adorn.json

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.ContentGenerator
import io.github.cottonmc.jsonfactory.output.model.Model
import io.github.cottonmc.jsonfactory.output.suffixed

object TableBlockModel : ContentGenerator("Table Block Model", "models/block", AdornPlugin.TABLE) {
    override fun generate(id: Identifier) = listOf(
        Model(
            parent = Identifier("adorn", "block/templates/table"),
            textures = mapOf(
                "planks" to Identifier.mc("block/${id.path}_planks")
            )
        ).suffixed("table"),
        Model(
            parent = Identifier("adorn", "block/templates/table_leg"),
            textures = mapOf(
                "planks" to Identifier.mc("block/${id.path}_planks")
            )
        ).suffixed("table_leg")
    )
}
