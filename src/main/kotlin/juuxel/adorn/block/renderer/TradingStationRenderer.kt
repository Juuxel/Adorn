package juuxel.adorn.block.renderer

import com.mojang.blaze3d.platform.GlStateManager
import juuxel.adorn.block.entity.TradingStationBlockEntity
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.resource.language.I18n
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult

@Environment(EnvType.CLIENT)
class TradingStationRenderer : BlockEntityRenderer<TradingStationBlockEntity>() {
    override fun render(be: TradingStationBlockEntity, x: Double, y: Double, z: Double, tickDelta: Float, i: Int) {
        super.render(be, x, y, z, tickDelta, i)
        val hitResult = renderManager.hitResult
        val lookingAtBlock = hitResult != null &&
                hitResult.type == HitResult.Type.BLOCK &&
                be.pos == (hitResult as BlockHitResult).blockPos

        val trade = be.trade
        if (!trade.isEmpty()) {
            GlStateManager.pushMatrix()
            GlStateManager.translated(x + 0.5, y + 1.2, z + 0.5)
            val playerAge = MinecraftClient.getInstance().player.age

            GlStateManager.pushMatrix()
            GlStateManager.rotatef((playerAge + tickDelta) * SELLING_ROTATION_MULTIPLIER, 0f, 1f, 0f)
            GlStateManager.scalef(0.6f, 0.6f, 0.6f)
            GlStateManager.translatef(0f, 0.3f, 0f)
            val itemRenderer = MinecraftClient.getInstance().itemRenderer
            itemRenderer.renderItem(trade.selling, ModelTransformation.Type.FIXED)
            GlStateManager.popMatrix()

            /*if (lookingAtBlock) {
                GlStateManager.rotatef((playerAge + tickDelta) * PRICE_ROTATION_MULTIPLIER, 0f, 1f, 0f)
                GlStateManager.translatef(0.55f, 0f, 0f)
                itemRenderer.renderItem(trade.price, ModelTransformation.Type.GROUND)
            }*/

            GlStateManager.popMatrix()
        }

        if (lookingAtBlock) {
            disableLightmap(true)
            val label1 = I18n.translate(LABEL_1, be.ownerName.copy().formatted(Formatting.GOLD).asFormattedString())
            renderName(be, label1, x, y + 0.9, z, 12)
            if (!be.trade.isEmpty()) {
                val label2 = I18n.translate(LABEL_2, be.trade.selling.toTextComponentWithCount().asFormattedString())
                val label3 = I18n.translate(LABEL_3, be.trade.price.toTextComponentWithCount().asFormattedString())
                renderName(be, label2, x, y + 0.9 - 0.25, z, 12)
                renderName(be, label3, x, y + 0.9 - 0.5, z, 12)
            }
            disableLightmap(false)
        }
    }

    private fun ItemStack.toTextComponentWithCount(): Text =
        LiteralText("${count}x ").append(toHoverableText())

    companion object {
        private const val SELLING_ROTATION_MULTIPLIER = 1.2f
        //private const val PRICE_ROTATION_MULTIPLIER = -2.5f

        private const val LABEL_1 = "block.adorn.trading_station.label.1"
        private const val LABEL_2 = "block.adorn.trading_station.label.2"
        private const val LABEL_3 = "block.adorn.trading_station.label.3"
    }
}
