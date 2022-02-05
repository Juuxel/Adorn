package juuxel.adorn.item

import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Formatting
import net.minecraft.world.World

open class ItemWithDescription(settings: Settings) : SimpleAdornItem(settings) {
    override fun appendTooltip(stack: ItemStack, world: World?, texts: MutableList<Text>, context: TooltipContext) {
        super.appendTooltip(stack, world, texts, context)
        texts.add(createDescriptionText("$translationKey.desc"))
    }

    companion object {
        fun createDescriptionText(translationKey: String): Text =
            TranslatableText(translationKey).styled {
                it.withItalic(true).withColor(Formatting.DARK_GRAY)
            }
    }
}
