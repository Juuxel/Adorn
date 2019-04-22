package juuxel.adorn.json

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.ContentGenerator
import io.github.cottonmc.jsonfactory.output.model.Model
import io.github.cottonmc.jsonfactory.output.suffixed

internal object KitchenCupboardItemModel : ContentGenerator("Kitchen Cupboard Item Model", "models/item", AdornPlugin.KITCHEN) {
    override fun generate(id: Identifier) = listOf(
        Model(
            parent = Identifier("adorn", "item/templates/kitchen_cupboard"),
            textures = mapOf(
                "planks" to Identifier.mc("block/${id.path}_planks")
            )
        ).suffixed("kitchen_cupboard")
    )
}
