package juuxel.adorn.compat.extrapieces.block

import com.shnupbups.extrapieces.blocks.PieceBlock
import juuxel.polyester.block.PolyesterBlock

interface AdornPieceBlock : PolyesterBlock, PieceBlock {
    override fun getBlock() = unwrap()
}
