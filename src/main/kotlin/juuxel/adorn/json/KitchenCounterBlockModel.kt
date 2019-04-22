package juuxel.adorn.json

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.ContentGenerator
import io.github.cottonmc.jsonfactory.output.model.Model
import io.github.cottonmc.jsonfactory.output.suffixed

object KitchenCounterBlockModel : ContentGenerator("Kitchen Counter Block Model", "models/block", AdornPlugin.KITCHEN) {
    override fun generate(id: Identifier) = listOf(
        Model(
            parent = Identifier("adorn", "block/templates/kitchen_counter"),
            textures = mapOf(
                "planks" to Identifier.mc("block/${id.path}_planks")
            )
        ).suffixed("kitchen_counter")
    )
}
