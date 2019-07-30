package juuxel.adorn.compat.extrapieces.piece

import com.shnupbups.extrapieces.blocks.PieceBlock
import com.shnupbups.extrapieces.core.PieceSet
import com.swordglowsblue.artifice.api.ArtificeResourcePack
import juuxel.adorn.Adorn
import juuxel.adorn.compat.extrapieces.block.DrawerPieceBlock
import juuxel.adorn.compat.extrapieces.getRegistryId

object DrawerPiece : SimpleRotatingPiece(Adorn.id("drawer")) {
    private val MODEL_PARENT = Adorn.id("block/templates/drawer")

    override fun getNew(set: PieceSet) = DrawerPieceBlock(set)

    override fun addBlockModels(pack: ArtificeResourcePack.ClientResourcePackBuilder, pb: PieceBlock) {
        val id = pb.getRegistryId()
        pack.addBlockModel(id) {
            it.parent(MODEL_PARENT)
            it.texture("planks", pb.set.mainTexture)
        }
    }
}
