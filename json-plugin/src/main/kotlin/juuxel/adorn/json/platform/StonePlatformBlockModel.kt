package juuxel.adorn.json.platform

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.AbstractContentGenerator
import io.github.cottonmc.jsonfactory.output.model.Model
import io.github.cottonmc.jsonfactory.output.suffixed
import juuxel.adorn.json.AdornPlugin

object StonePlatformBlockModel : AbstractContentGenerator("platform.stone.block_model", "models/block", AdornPlugin.PLATFORM) {
    override fun generate(id: Identifier) = listOf(
        Model(
            parent = Identifier("adorn", "block/templates/platform"),
            textures = mapOf(
                "texture" to Identifier.mc("block/${id.path}")
            )
        ).suffixed("platform")
    )
}
