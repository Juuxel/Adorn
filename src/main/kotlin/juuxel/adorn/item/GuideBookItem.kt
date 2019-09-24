package juuxel.adorn.item

import io.github.cottonmc.cotton.gui.client.ClientCottonScreen
import juuxel.adorn.gui.screen.GuideScreenDescription
import juuxel.adorn.resources.GuideManager
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.stat.Stats
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class GuideBookItem(private val guideId: Identifier, settings: Item.Settings) : Item(settings) {
    override fun use(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (world.isClient) {
            MinecraftClient.getInstance().openScreen(
                ClientCottonScreen(
                    GuideScreenDescription(GuideManager[guideId])
                )
            )
        }
        player.incrementStat(Stats.USED.getOrCreateStat(this))

        return TypedActionResult(ActionResult.SUCCESS, player.getStackInHand(hand))
    }

    @Environment(EnvType.CLIENT)
    override fun hasEnchantmentGlint(stack: ItemStack) = true
}
