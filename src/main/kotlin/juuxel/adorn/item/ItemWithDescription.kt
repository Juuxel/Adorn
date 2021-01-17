package juuxel.adorn.item

import net.minecraft.client.item.TooltipContext
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Formatting
import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

open class ItemWithDescription(settings: Settings) : Item(settings) {
    @OnlyIn(Dist.CLIENT)
    override fun appendTooltip(stack: ItemStack, world: World?, texts: MutableList<Text>, context: TooltipContext) {
        super.appendTooltip(stack, world, texts, context)
        texts.add(createDescriptionText("$translationKey.desc"))
    }

    override fun isIn(group: ItemGroup?) = AdornItems.isIn(group, this)

    companion object {
        fun createDescriptionText(key: String): Text =
            TranslatableText(key).styled {
                it.withItalic(true).withColor(Formatting.DARK_GRAY)
            }
    }
}
