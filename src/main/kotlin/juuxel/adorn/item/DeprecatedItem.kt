package juuxel.adorn.item

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class DeprecatedItem(private val new: Item) : Item(Item.Settings()) {
    override fun getTranslationKey() = new.translationKey

    override fun getName(stack: ItemStack) =
        TranslatableText("item.adorn.deprecated_item.name", super.getName(stack))

    override fun use(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stack = player.getStackInHand(hand)
        val newStack = ItemStack(new, stack.count)
        newStack.tag = stack.tag
        player.setStackInHand(hand, newStack)
        return TypedActionResult(ActionResult.SUCCESS, newStack)
    }

    @Environment(EnvType.CLIENT)
    override fun appendTooltip(
        stack: ItemStack,
        world: World?,
        texts: MutableList<Text>,
        context: TooltipContext
    ) {
        super.appendTooltip(stack, world, texts, context)
        texts.add(TranslatableText("item.adorn.deprecated_item.desc.1"))
        texts.add(TranslatableText("item.adorn.deprecated_item.desc.2", LiteralText(MinecraftClient.getInstance().options.keyUse.localizedName)))
    }
}
