package juuxel.adorn.compat.extrapieces.piece

import com.shnupbups.extrapieces.blocks.PieceBlock
import com.shnupbups.extrapieces.core.PieceType
import com.swordglowsblue.artifice.api.ArtificeResourcePack
import juuxel.adorn.compat.extrapieces.getRegistryId
import net.minecraft.util.Identifier

abstract class SimplePiece(id: Identifier, private val modelTemplate: Identifier) : AdornPiece(id) {
    override fun addBlockModels(pack: ArtificeResourcePack.ClientResourcePackBuilder, pb: PieceBlock) {
        val id = pb.getRegistryId()
        pack.addBlockModel(id) {
            it.parent(modelTemplate)
            it.texture("texture", pb.set.mainTexture)
        }
    }
}
