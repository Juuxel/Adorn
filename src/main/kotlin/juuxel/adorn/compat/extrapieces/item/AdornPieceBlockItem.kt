package juuxel.adorn.compat.extrapieces.item

import com.shnupbups.extrapieces.blocks.PieceBlock
import com.shnupbups.extrapieces.blocks.PieceBlockItem
import juuxel.polyester.block.PolyesterBlock
import juuxel.polyester.item.PolyesterItem
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.world.World

open class AdornPieceBlockItem(pb: PieceBlock, settings: Settings) : PieceBlockItem(pb, settings) {
    override fun appendTooltip(stack: ItemStack, world: World?, texts: MutableList<Text>, context: TooltipContext) {
        super.appendTooltip(stack, world, texts, context)
        val pb = pieceBlock
        if (pb is PolyesterBlock && pb.hasDescription) {
            PolyesterItem.appendTooltipToList(texts, this, pb.descriptionKey)
        }
    }
}
