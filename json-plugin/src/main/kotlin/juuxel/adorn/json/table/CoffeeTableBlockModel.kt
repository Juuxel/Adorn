package juuxel.adorn.json.table

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.AbstractContentGenerator
import io.github.cottonmc.jsonfactory.output.model.Model
import io.github.cottonmc.jsonfactory.output.suffixed
import juuxel.adorn.json.AdornPlugin

object CoffeeTableBlockModel : AbstractContentGenerator("coffee_table.block_model", "models/block",
    AdornPlugin.TABLE
) {
    override fun generate(id: Identifier) = listOf(
        Model(
            parent = Identifier("adorn", "block/templates/coffee_table"),
            textures = mapOf(
                "planks" to Identifier.mc("block/${id.path}_planks"),
                "leg" to Identifier.mc("block/${id.path}_log"),
                "rim" to Identifier.mc("block/${id.path}_log")
            )
        ).suffixed("coffee_table")
    )
}
