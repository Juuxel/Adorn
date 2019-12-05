package juuxel.adorn.json.lamp

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.AbstractContentGenerator
import io.github.cottonmc.jsonfactory.output.model.ModelVariant
import io.github.cottonmc.jsonfactory.output.model.MultipartBlockState
import io.github.cottonmc.jsonfactory.output.model.MultipartBlockState.Multipart
import io.github.cottonmc.jsonfactory.output.model.MultipartBlockState.When
import io.github.cottonmc.jsonfactory.output.suffixed
import juuxel.adorn.json.AdornPlugin

object TallLampBlockState : AbstractContentGenerator("Tall Lamp Block State", "blockstates",
    AdornPlugin.OTHER
) {
    override fun generate(id: Identifier) = listOf(
        MultipartBlockState(
            multipart = listOf(
                Multipart(
                    `when` = When("half", "upper"),
                    apply = ModelVariant(id.wrapPath("block/", "_tall_lamp_upper"))
                ),
                Multipart(
                    `when` = When("half", "lower"),
                    apply = ModelVariant(Identifier("adorn", "block/templates/tall_lamp_lower"))
                )
            )
        ).suffixed("tall_lamp")
    )
}
