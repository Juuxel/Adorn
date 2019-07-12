package juuxel.adorn.item

import net.minecraft.client.item.TooltipContext
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.world.World

class DeprecatedItem(private val new: Item) : Item(Item.Settings()) {
    override fun getTranslationKey() = new.translationKey

    override fun appendTooltip(
        stack: ItemStack,
        world: World?,
        texts: MutableList<Text>,
        context: TooltipContext
    ) {
        super.appendTooltip(stack, world, texts, context)
        texts.add(TranslatableText("item.adorn.deprecated_item.desc.1"))
        texts.add(TranslatableText("item.adorn.deprecated_item.desc.2"))
    }
}
