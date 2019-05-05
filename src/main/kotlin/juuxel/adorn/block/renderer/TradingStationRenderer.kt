package juuxel.adorn.block.renderer

import juuxel.adorn.block.entity.TradingStationBlockEntity
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.item.ItemStack
import net.minecraft.text.StringTextComponent
import net.minecraft.text.TextComponent
import net.minecraft.text.TextFormat
import net.minecraft.text.TranslatableTextComponent
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
            yield(TranslatableTextComponent(
                "block.adorn.trading_station.label.1",
                be.ownerName.copy().applyFormat(TextFormat.GOLD)
            ))

            if (!be.trade.isEmpty()) {
                yield(
                    TranslatableTextComponent(
                        "block.adorn.trading_station.label.2",
                        be.trade.selling.toTextComponentWithCount()
                    )
                )
                yield(
                    TranslatableTextComponent(
                        "block.adorn.trading_station.label.3",
                        be.trade.price.toTextComponentWithCount()
                    )
                )
            }
        }.map(TextComponent::getFormattedText)

    private fun ItemStack.toTextComponentWithCount(): TextComponent =
        StringTextComponent("${amount}x ").append(toTextComponent())
}
