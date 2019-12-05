package juuxel.adorn.json.lamp

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.AbstractContentGenerator
import io.github.cottonmc.jsonfactory.output.model.Model
import io.github.cottonmc.jsonfactory.output.suffixed
import juuxel.adorn.json.AdornPlugin

object TallLampBlockModel : AbstractContentGenerator("Tall Lamp Block Model", "models/block",
    AdornPlugin.OTHER
) {
    override fun generate(id: Identifier) = listOf(
        Model(
            parent = Identifier("adorn", "block/templates/tall_lamp_upper"),
            textures = mapOf(
                "wool" to Identifier.mc("block/${id.path}_wool")
            )
        ).suffixed("tall_lamp_upper")
    )
}
