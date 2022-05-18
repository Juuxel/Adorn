package juuxel.adorn.item

import net.minecraft.block.Block
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.WallStandingBlockItem
import net.minecraft.text.Text
import net.minecraft.world.World

class WallBlockItemWithDescription(ground: Block, wall: Block, settings: Settings) : WallStandingBlockItem(ground, wall, settings) {
    override fun appendTooltip(stack: ItemStack, world: World?, texts: MutableList<Text>, context: TooltipContext) {
        super.appendTooltip(stack, world, texts, context)
        texts.add(ItemWithDescription.createDescriptionText("$translationKey.description"))
    }

    override fun isIn(group: ItemGroup?) = AdornItems.isIn(group, this)
}
