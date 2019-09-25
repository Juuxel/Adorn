package juuxel.adorn.item

import juuxel.adorn.gui.screen.GuideScreenDescription
import juuxel.adorn.gui.screen.TickingClientScreen
import juuxel.adorn.resources.GuideManager
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.stat.Stats
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.*
import net.minecraft.world.World

class GuideBookItem(private val guideId: Identifier, settings: Item.Settings) : Item(settings) {
    override fun use(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (world.isClient) {
            MinecraftClient.getInstance().openScreen(
                TickingClientScreen(
                    GuideScreenDescription(GuideManager[guideId])
                )
            )
        }
        player.incrementStat(Stats.USED.getOrCreateStat(this))

        return TypedActionResult(ActionResult.SUCCESS, player.getStackInHand(hand))
    }

    @Environment(EnvType.CLIENT)
    override fun hasEnchantmentGlint(stack: ItemStack) = true

    @Environment(EnvType.CLIENT)
    override fun appendTooltip(stack: ItemStack, world: World?, texts: MutableList<Text>, context: TooltipContext) {
        super.appendTooltip(stack, world, texts, context)
        if (guideId in GuideManager) {
            texts.add(TranslatableText("book.byAuthor", GuideManager[guideId].author).formatted(Formatting.GRAY))
        }
    }
}
