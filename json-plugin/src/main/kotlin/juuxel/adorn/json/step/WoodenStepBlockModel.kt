package juuxel.adorn.json.step

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.ContentGenerator
import io.github.cottonmc.jsonfactory.output.model.Model
import io.github.cottonmc.jsonfactory.output.suffixed
import juuxel.adorn.json.AdornPlugin

object WoodenStepBlockModel : ContentGenerator("Wooden Step Block Model", "models/block", AdornPlugin.STEP) {
    override fun generate(id: Identifier) = listOf(
        Model(
            parent = Identifier("adorn", "block/templates/step"),
            textures = mapOf(
                "texture" to Identifier.mc("block/${id.path}_planks")
            )
        ).suffixed("step")
    )
}
