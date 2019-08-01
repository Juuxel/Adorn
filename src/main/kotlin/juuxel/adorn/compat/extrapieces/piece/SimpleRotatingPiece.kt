package juuxel.adorn.compat.extrapieces.piece

import com.shnupbups.extrapieces.ExtraPieces
import com.shnupbups.extrapieces.blocks.PieceBlock
import com.shnupbups.extrapieces.core.PieceType
import com.swordglowsblue.artifice.api.ArtificeResourcePack
import juuxel.adorn.compat.extrapieces.AdornPieces
import juuxel.adorn.compat.extrapieces.getRegistryId
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction

abstract class SimpleRotatingPiece(id: Identifier) : AdornPiece(id) {
    override fun addBlockstate(pack: ArtificeResourcePack.ClientResourcePackBuilder, pb: PieceBlock) {
        val id = pb.getRegistryId()
        fun getYRotation(facing: Direction): Int = when (facing) {
            Direction.EAST -> 0
            Direction.SOUTH -> 90
            Direction.WEST -> 180
            Direction.NORTH -> 270

            else -> 0
        }

        pack.addBlockState(id) {
            for (facing in AdornPieces.HORIZONTAL_DIRECTIONS) {
                it.variant("facing=" + facing.asString()) { variant ->
                    variant.model(ExtraPieces.prependToPath(id, "block/"))
                    variant.rotationY(getYRotation(facing))
                }
            }
        }
    }
}
