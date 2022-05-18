package juuxel.adorn.item

import juuxel.adorn.block.BlockWithDescription
import net.minecraft.block.Block
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.TallBlockItem
import net.minecraft.text.Text
import net.minecraft.world.World

open class AdornTallBlockItem(block: Block, settings: Settings) : TallBlockItem(block, settings) {
    override fun isIn(group: ItemGroup?) = AdornItems.isIn(group, this)

    override fun appendTooltip(stack: ItemStack?, world: World?, texts: MutableList<Text>, context: TooltipContext?) {
        super.appendTooltip(stack, world, texts, context)

        (block as? BlockWithDescription)?.let {
            texts.add(ItemWithDescription.createDescriptionText(it.descriptionKey))
        }
    }
}
