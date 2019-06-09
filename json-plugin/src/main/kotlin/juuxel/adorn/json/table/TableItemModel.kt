package juuxel.adorn.json.table

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.AbstractContentGenerator
import io.github.cottonmc.jsonfactory.output.model.Model
import io.github.cottonmc.jsonfactory.output.suffixed
import juuxel.adorn.json.AdornPlugin

internal object TableItemModel : AbstractContentGenerator("table.item_model", "models/item",
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
