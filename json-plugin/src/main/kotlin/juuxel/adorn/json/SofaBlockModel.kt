package juuxel.adorn.json

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.ContentGenerator
import io.github.cottonmc.jsonfactory.output.model.Model
import io.github.cottonmc.jsonfactory.output.suffixed

internal object SofaBlockModel : ContentGenerator("Sofa Block Model", "models/block",
    AdornPlugin.SOFA
) {
    override fun generate(id: Identifier) = listOf(
        Model(
            parent = Identifier("adorn", "block/templates/sofa_center"),
            textures = mapOf(
                "wool" to Identifier.mc("block/${id.path}_wool")
            )
        ).suffixed("sofa_center"),
        Model(
            parent = Identifier("adorn", "block/templates/sofa_arm_left"),
            textures = mapOf(
                "wool" to Identifier.mc("block/${id.path}_wool")
            )
        ).suffixed("sofa_arm_left"),
        Model(
            parent = Identifier("adorn", "block/templates/sofa_arm_right"),
            textures = mapOf(
                "wool" to Identifier.mc("block/${id.path}_wool")
            )
        ).suffixed("sofa_arm_right"),
        Model(
            parent = Identifier("adorn", "block/templates/sofa_corner_left"),
            textures = mapOf(
                "wool" to Identifier.mc("block/${id.path}_wool")
            )
        ).suffixed("sofa_corner_left"),
        Model(
            parent = Identifier("adorn", "block/templates/sofa_corner_right"),
            textures = mapOf(
                "wool" to Identifier.mc("block/${id.path}_wool")
            )
        ).suffixed("sofa_corner_right")
    )
}
