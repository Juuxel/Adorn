package juuxel.adorn.compat.extrapieces.piece

import com.shnupbups.extrapieces.blocks.PieceBlock
import com.shnupbups.extrapieces.core.PieceType
import juuxel.adorn.compat.extrapieces.item.AdornPieceBlockItem
import net.minecraft.item.Item
import net.minecraft.util.Identifier

abstract class AdornPiece(id: Identifier) : PieceType(id) {
    override fun getBlockItem(pb: PieceBlock) = AdornPieceBlockItem(pb, Item.Settings())
}
