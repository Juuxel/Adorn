package juuxel.adorn.json.kitchen

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.AbstractContentGenerator
import io.github.cottonmc.jsonfactory.output.model.Model
import io.github.cottonmc.jsonfactory.output.suffixed
import juuxel.adorn.json.AdornPlugin

object KitchenCounterBlockModel : AbstractContentGenerator("kitchen_counter.block_model", "models/block",
    AdornPlugin.KITCHEN
) {
    override fun generate(id: Identifier) = listOf(
        Model(
            parent = Identifier("adorn", "block/templates/kitchen_counter"),
            textures = mapOf(
                "planks" to Identifier.mc("block/${id.path}_planks")
            )
        ).suffixed("kitchen_counter"),
        Model(
            parent = Identifier("adorn", "block/templates/kitchen_counter_connection_left"),
            textures = mapOf(
                "planks" to Identifier.mc("block/${id.path}_planks")
            )
        ).suffixed("kitchen_counter_connection_left"),
        Model(
            parent = Identifier("adorn", "block/templates/kitchen_counter_connection_right"),
            textures = mapOf(
                "planks" to Identifier.mc("block/${id.path}_planks")
            )
        ).suffixed("kitchen_counter_connection_right")
    )
}
