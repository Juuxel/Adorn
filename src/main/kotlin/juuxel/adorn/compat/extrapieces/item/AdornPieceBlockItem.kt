package juuxel.adorn.compat.extrapieces.item

import com.shnupbups.extrapieces.blocks.PieceBlock
import com.shnupbups.extrapieces.blocks.PieceBlockItem
import juuxel.adorn.block.BlockWithDescription
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Formatting
import net.minecraft.world.World

open class AdornPieceBlockItem(pb: PieceBlock, settings: Settings) : PieceBlockItem(pb, settings) {
    override fun appendTooltip(stack: ItemStack, world: World?, texts: MutableList<Text>, context: TooltipContext) {
        super.appendTooltip(stack, world, texts, context)
        val pb = pieceBlock
        if (pb is BlockWithDescription) {
            texts.add(TranslatableText(pb.descriptionKey).styled {
                it.isItalic = true
                it.color = Formatting.DARK_GRAY
            })
        }
    }
}
