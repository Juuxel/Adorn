package juuxel.adorn.block.renderer

import com.mojang.blaze3d.systems.RenderSystem
import juuxel.adorn.block.entity.TradingStationBlockEntity
import juuxel.adorn.util.Colors
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.resource.language.I18n
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.util.math.Vector3f
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult

@Environment(EnvType.CLIENT)
class TradingStationRenderer(dispatcher: BlockEntityRenderDispatcher) : BlockEntityRenderer<TradingStationBlockEntity>(dispatcher) {
    override fun render(be: TradingStationBlockEntity, tickDelta: Float, matrix: MatrixStack, vertexConsumerProvider: VertexConsumerProvider, i: Int, j: Int) {
        val hitResult = blockEntityRenderDispatcher.crosshairTarget
        val lookingAtBlock = hitResult != null &&
                hitResult.type == HitResult.Type.BLOCK &&
                be.pos == (hitResult as BlockHitResult).blockPos

        val trade = be.trade
        if (!trade.isEmpty()) {
            matrix.push()
            matrix.translate(0.5, 1.2, 0.5)
            val playerAge = MinecraftClient.getInstance().player!!.age

            matrix.push()
            matrix.multiply(Vector3f.POSITIVE_Y.getRotationQuaternion((playerAge + tickDelta) * SELLING_ROTATION_MULTIPLIER))
            matrix.scale(0.6f, 0.6f, 0.6f)
            matrix.translate(0.0, 0.3, 0.0)
            val itemRenderer = MinecraftClient.getInstance().itemRenderer
            itemRenderer.method_23178(trade.selling, ModelTransformation.Type.FIXED, i, j, matrix, vertexConsumerProvider)
            matrix.pop()

            /*if (lookingAtBlock) {
                GlStateManager.rotatef((playerAge + tickDelta) * PRICE_ROTATION_MULTIPLIER, 0f, 1f, 0f)
                GlStateManager.translatef(0.55f, 0f, 0f)
                itemRenderer.renderItem(trade.price, ModelTransformation.Type.GROUND)
            }*/

            matrix.pop()
        }

        if (lookingAtBlock) {
            //disableLightmap(true)
            val label1 = I18n.translate(LABEL_1, be.ownerName.copy().formatted(Formatting.GOLD).asFormattedString())
            renderLabel(be, label1, 0.0,  0.9, 0.0, 12, matrix, vertexConsumerProvider, i)
            if (!be.trade.isEmpty()) {
                val label2 = I18n.translate(LABEL_2, be.trade.selling.toTextComponentWithCount().asFormattedString())
                val label3 = I18n.translate(LABEL_3, be.trade.price.toTextComponentWithCount().asFormattedString())
                renderLabel(be, label2, 0.0, 0.9 - 0.25, 0.0, 12, matrix, vertexConsumerProvider, i)
                renderLabel(be, label3, 0.0, 0.9 - 0.5, 0.0, 12, matrix, vertexConsumerProvider, i)
            }
            //disableLightmap(false)
        }
    }

    private fun disableLightmap(b: Boolean) {
        RenderSystem.activeTexture(33985)
        if (b) {
            RenderSystem.disableTexture()
        } else {
            RenderSystem.enableTexture()
        }
        RenderSystem.activeTexture(33984)
    }

    private fun renderLabel(be: TradingStationBlockEntity, name: String, x: Double, y: Double, z: Double, maxDistance: Int, matrix: MatrixStack, vcp: VertexConsumerProvider, i: Int) {
        val camera = blockEntityRenderDispatcher.camera
        val dist = be.getSquaredDistance(camera.pos.x, camera.pos.y, camera.pos.z)
        if (dist < maxDistance * maxDistance) {
            val yaw = camera.yaw
            val pitch = camera.pitch
            matrix.push()
            matrix.translate(x + 0.5, y + 1.5, z + 0.5)
            matrix.multiply(camera.method_23767())
            matrix.scale(-0.025f, -0.025f, +0.025f)

            val matrixModel = matrix.peek().model
            val opacity = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25f)
            val opacityAsAlpha = (opacity * 255.0f).toInt() shl 24
            val textRenderer = blockEntityRenderDispatcher.textRenderer
            val textX = -textRenderer.getStringWidth(name) / 2f
            textRenderer.draw(name, textX, 0f, Colors.WHITE, false, matrixModel, vcp, true, opacityAsAlpha, i)

            matrix.pop()
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
