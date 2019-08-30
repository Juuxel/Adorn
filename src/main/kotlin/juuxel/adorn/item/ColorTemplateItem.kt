package juuxel.adorn.item

import juuxel.adorn.block.Colorable
import juuxel.adorn.lib.NbtKeys
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.util.NbtType
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.nbt.AbstractNumberTag
import net.minecraft.nbt.ListTag
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.ActionResult
import net.minecraft.util.Formatting
import net.minecraft.world.World

class ColorTemplateItem(settings: Settings) : Item(settings) {
    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val world = context.world
        val pos = context.blockPos
        val block = world.getBlockState(pos).block

        if (block is Colorable) {
            if (!world.isClient) {
                val colorTag = getColor(context.stack)
                if (colorTag != null) {
                    block.applyColor(
                        world, pos,
                        world.getBlockState(pos),
                        (colorTag[0] as AbstractNumberTag).int,
                        (colorTag[1] as AbstractNumberTag).int,
                        (colorTag[2] as AbstractNumberTag).int
                    )
                }
            }

            return ActionResult.SUCCESS
        }

        return ActionResult.PASS
    }

    private fun getColor(stack: ItemStack): ListTag? =
        stack.tag?.getList(NbtKeys.COLOR_TEMPLATE_COLOR, NbtType.INT)

    @Environment(EnvType.CLIENT)
    override fun appendTooltip(stack: ItemStack, world: World?, texts: MutableList<Text>, context: TooltipContext) {
        super.appendTooltip(stack, world, texts, context)
        texts.add(TranslatableText("$translationKey.desc").styled {
            it.isItalic = true
            it.color = Formatting.DARK_GRAY
        })
    }
}
