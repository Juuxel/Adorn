package juuxel.adorn.json

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.ContentGenerator
import io.github.cottonmc.jsonfactory.output.model.Model
import io.github.cottonmc.jsonfactory.output.suffixed

internal object ChairItemModel : ContentGenerator("Chair Item Model", "models/item", AdornPlugin.CHAIR) {
    override fun generate(id: Identifier) = listOf(
        Model(
            parent = Identifier("adorn", "item/templates/chair"),
            textures = mapOf(
                "planks" to Identifier.mc("block/${id.path}_planks")
            )
        ).suffixed("chair")
    )
}
