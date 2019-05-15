package juuxel.adorn.json

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.ContentGenerator
import io.github.cottonmc.jsonfactory.output.model.Model
import io.github.cottonmc.jsonfactory.output.suffixed

internal object TableItemModel : ContentGenerator("Table Item Model", "models/item",
    AdornPlugin.TABLE
) {
    override fun generate(id: Identifier) = listOf(
        Model(
            parent = Identifier("adorn", "item/templates/table"),
            textures = mapOf(
                "planks" to Identifier.mc("block/${id.path}_planks"),
                "log" to Identifier.mc("block/${id.path}_log")
            )
        ).suffixed("table")
    )
}
