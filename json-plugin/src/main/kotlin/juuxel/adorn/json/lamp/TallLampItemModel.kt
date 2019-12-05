package juuxel.adorn.json.lamp

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.AbstractContentGenerator
import io.github.cottonmc.jsonfactory.output.model.Model
import io.github.cottonmc.jsonfactory.output.suffixed
import juuxel.adorn.json.AdornPlugin

object TallLampItemModel : AbstractContentGenerator("Tall Lamp Item Model", "models/item",
    AdornPlugin.OTHER
) {
    override fun generate(id: Identifier) = listOf(
        Model(
            parent = Identifier("adorn", "item/templates/tall_lamp"),
            textures = mapOf(
                "wool" to Identifier.mc("block/${id.path}_wool")
            )
        ).suffixed("tall_lamp")
    )
}
