package juuxel.adorn.block.renderer

import juuxel.adorn.block.entity.TradingStationBlockEntity
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.ChatFormat
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.item.ItemStack
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextComponent
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult

@Environment(EnvType.CLIENT)
class TradingStationRenderer : BlockEntityRenderer<TradingStationBlockEntity>() {
    override fun render(be: TradingStationBlockEntity, x: Double, y: Double, z: Double, tickDelta: Float, i: Int) {
        super.render(be, x, y, z, tickDelta, i)
        val hitResult = renderManager.hitResult

        if (hitResult != null && hitResult.type == HitResult.Type.BLOCK && be.pos == (hitResult as BlockHitResult).blockPos) {
            disableLightmap(true)
            for ((row, text) in getLabelRows(be).withIndex()) {
                renderName(be, text, x, y + 0.25 - 0.25 * row, z, 12)
            }
            disableLightmap(false)
        }
    }

    private fun getLabelRows(be: TradingStationBlockEntity): Sequence<String> =
        sequence {
            yield(TranslatableComponent(
                "block.adorn.trading_station.label.1",
                be.ownerName.copy().applyFormat(ChatFormat.GOLD)
            ))

            if (!be.trade.isEmpty()) {
                yield(
                    TranslatableComponent(
                        "block.adorn.trading_station.label.2",
                        be.trade.selling.toTextComponentWithCount()
                    )
                )
                yield(
                    TranslatableComponent(
                        "block.adorn.trading_station.label.3",
                        be.trade.price.toTextComponentWithCount()
                    )
                )
            }
        }.map(Component::getFormattedText)

    private fun ItemStack.toTextComponentWithCount(): Component =
        TextComponent("${amount}x ").append(toTextComponent())
}
