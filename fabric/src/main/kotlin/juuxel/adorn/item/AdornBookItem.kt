package juuxel.adorn.item

import juuxel.adorn.client.resources.BookManager
import juuxel.adorn.lib.AdornNetworking
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.stat.Stats
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.ActionResult
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class AdornBookItem(private val bookId: Identifier, settings: Settings) : Item(settings) {
    override fun use(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (!world.isClient) {
            AdornNetworking.sendOpenBookPacket(player, bookId)
        }
        player.incrementStat(Stats.USED.getOrCreateStat(this))

        return TypedActionResult(ActionResult.SUCCESS, player.getStackInHand(hand))
    }

//    @Environment(EnvType.CLIENT)
//    override fun hasEnchantmentGlint(stack: ItemStack) = true

    override fun appendTooltip(stack: ItemStack, world: World?, texts: MutableList<Text>, context: TooltipContext) {
        super.appendTooltip(stack, world, texts, context)
        if (bookId in BookManager) {
            texts.add(TranslatableText("book.byAuthor", BookManager[bookId].author).formatted(Formatting.GRAY))
        }
    }

    override fun isIn(group: ItemGroup?) = AdornItems.isIn(group, this)
}
