package juuxel.adorn.compat.extrapieces

import com.shnupbups.extrapieces.core.PieceSet
import com.swordglowsblue.artifice.api.builder.assets.BlockStateBuilder
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.util.Identifier

@Environment(EnvType.CLIENT)
object AdornPiecesClient {
    fun addCarpets(state: BlockStateBuilder, set: PieceSet) {
        if (!AdornPieces.isCarpetingEnabled(set)) return

        val colors = arrayOf(
            "red",
            "black",
            "green",
            "brown",
            "blue",
            "purple",
            "cyan",
            "light_gray",
            "gray",
            "pink",
            "lime",
            "yellow",
            "light_blue",
            "magenta",
            "orange",
            "white"
        )

        // Carpets
        for (color in colors) {
            state.multipartCase {
                it.`when`("carpet", color)
                it.apply { variant -> variant.model(Identifier("block/${color}_carpet")) }
            }
        }
    }
}
