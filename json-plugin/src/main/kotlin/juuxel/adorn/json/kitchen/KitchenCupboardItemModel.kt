package juuxel.adorn.json.kitchen

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.AbstractContentGenerator
import io.github.cottonmc.jsonfactory.output.model.Model
import io.github.cottonmc.jsonfactory.output.suffixed
import juuxel.adorn.json.AdornPlugin

internal object KitchenCupboardItemModel : AbstractContentGenerator("kitchen_cupboard.item_model", "models/item",
    AdornPlugin.KITCHEN
) {
    override fun generate(id: Identifier) = listOf(
        Model(
            parent = Identifier("adorn", "item/templates/kitchen_cupboard"),
            textures = mapOf(
                "planks" to Identifier.mc("block/${id.path}_planks")
            )
        ).suffixed("kitchen_cupboard")
    )
}
