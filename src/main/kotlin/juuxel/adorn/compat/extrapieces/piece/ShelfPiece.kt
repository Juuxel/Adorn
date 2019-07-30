package juuxel.adorn.compat.extrapieces.piece

import com.shnupbups.extrapieces.blocks.PieceBlock
import com.shnupbups.extrapieces.core.PieceSet
import com.swordglowsblue.artifice.api.ArtificeResourcePack
import juuxel.adorn.Adorn
import juuxel.adorn.compat.extrapieces.block.ShelfPieceBlock
import juuxel.adorn.compat.extrapieces.getRegistryId
import net.minecraft.util.Identifier

object ShelfPiece : SimpleRotatingPiece(Adorn.id("shelf")) {
    private val MODEL_PARENT = Adorn.id("block/templates/shelf")

    override fun getNew(set: PieceSet) = ShelfPieceBlock(set)

    override fun addBlockModels(pack: ArtificeResourcePack.ClientResourcePackBuilder, pb: PieceBlock) {
        val id = pb.getRegistryId()
        pack.addBlockModel(id) {
            it.parent(MODEL_PARENT)
            it.texture("texture", pb.set.mainTexture)
            it.texture("supports", pb.set.mainTexture)
        }
    }

    override fun getTagId() = Adorn.id("shelves")
}
