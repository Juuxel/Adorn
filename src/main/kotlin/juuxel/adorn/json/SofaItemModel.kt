package juuxel.adorn.json

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.ContentGenerator
import io.github.cottonmc.jsonfactory.output.model.Model
import io.github.cottonmc.jsonfactory.output.suffixed

internal object SofaItemModel : ContentGenerator("Sofa Item Model", "models/item", AdornPlugin.SOFA) {
    override fun generate(id: Identifier) = listOf(
        Model(
            parent = Identifier("adorn", "item/templates/sofa"),
            textures = mapOf(
                "wool" to Identifier.mc("block/${id.path}_wool")
            )
        ).suffixed("sofa")
    )
}
