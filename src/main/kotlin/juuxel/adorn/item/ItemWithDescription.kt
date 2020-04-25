package juuxel.adorn.item

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Formatting
import net.minecraft.world.World

open class ItemWithDescription(settings: Settings) : Item(settings) {
    @Environment(EnvType.CLIENT)
    override fun appendTooltip(stack: ItemStack, world: World?, texts: MutableList<Text>, context: TooltipContext) {
        super.appendTooltip(stack, world, texts, context)
        texts.add(TranslatableText("$translationKey.desc").method_27694 {
            it.setItalic(true).setColor(Formatting.DARK_GRAY)
        })
    }

    override fun isIn(group: ItemGroup?) = super.isIn(group) || group === AdornItems.GROUP
}
