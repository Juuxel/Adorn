package juuxel.adorn.item

import net.minecraft.block.Block
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ItemStack
import net.minecraft.item.VerticallyAttachableBlockItem
import net.minecraft.text.Text
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class VerticallyAttachableBlockItemWithDescription(ground: Block, wall: Block, settings: Settings, attachmentDirection: Direction) :
    VerticallyAttachableBlockItem(ground, wall, settings, attachmentDirection) {
    override fun appendTooltip(stack: ItemStack, world: World?, texts: MutableList<Text>, context: TooltipContext) {
        super.appendTooltip(stack, world, texts, context)
        texts.add(ItemWithDescription.createDescriptionText("$translationKey.description"))
    }
}
