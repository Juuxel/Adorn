package juuxel.adorn.client.renderer

import juuxel.adorn.block.entity.TradingStationBlockEntity
import juuxel.adorn.platform.PlatformBridges
import juuxel.adorn.util.Colors
import juuxel.adorn.util.color
import juuxel.adorn.util.getSquaredDistance
import juuxel.adorn.util.toTextWithCount
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.block.entity.BlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Formatting
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.Vec3f

@Environment(EnvType.CLIENT)
class TradingStationRenderer(context: BlockEntityRendererFactory.Context) : BlockEntityRenderer<TradingStationBlockEntity> {
    private val dispatcher = context.renderDispatcher
    private val textRenderer = context.textRenderer

    override fun render(
        be: TradingStationBlockEntity, tickDelta: Float,
        matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int, overlay: Int
    ) {
        val hitResult = dispatcher.crosshairTarget
        val lookingAtBlock = hitResult != null &&
            hitResult.type == HitResult.Type.BLOCK &&
            be.pos == (hitResult as BlockHitResult).blockPos

        val trade = be.trade
        if (!trade.isEmpty()) {
            matrices.push()
            matrices.translate(0.5, 1.2, 0.5)
            val playerAge = MinecraftClient.getInstance().player!!.age

            matrices.push()
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((playerAge + tickDelta) * SELLING_ROTATION_MULTIPLIER))
            matrices.scale(0.6f, 0.6f, 0.6f)
            matrices.translate(0.0, 0.3, 0.0)
            val itemRenderer = MinecraftClient.getInstance().itemRenderer
            itemRenderer.renderItem(trade.selling, ModelTransformation.Mode.FIXED, light, overlay, matrices, vertexConsumers, 0)
            matrices.pop()

            /*if (lookingAtBlock) {
                GlStateManager.rotatef((playerAge + tickDelta) * PRICE_ROTATION_MULTIPLIER, 0f, 1f, 0f)
                GlStateManager.translatef(0.55f, 0f, 0f)
                itemRenderer.renderItem(trade.price, ModelTransformation.Type.GROUND)
            }*/

            matrices.pop()
        }

        if (lookingAtBlock && PlatformBridges.config.client.showTradingStationTooltips) {
            val label1 = TranslatableText(OWNER_LABEL, be.ownerName.copy().formatted(Formatting.GOLD))
            renderLabel(be, label1, 0.0, 0.9, 0.0, 12, matrices, vertexConsumers, light)
            if (!be.trade.isEmpty()) {
                val label2 = TranslatableText(SELLING_LABEL, be.trade.selling.toTextWithCount())
                val label3 = TranslatableText(PRICE_LABEL, be.trade.price.toTextWithCount())
                renderLabel(be, label2, 0.0, 0.9 - 0.25, 0.0, 12, matrices, vertexConsumers, light)
                renderLabel(be, label3, 0.0, 0.9 - 0.5, 0.0, 12, matrices, vertexConsumers, light)
            }
        }
    }

    private fun renderLabel(
        be: BlockEntity, label: Text,
        x: Double, y: Double, z: Double,
        maxDistance: Int,
        matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int
    ) {
        val camera = dispatcher.camera

        val dist = be.getSquaredDistance(camera.pos.x, camera.pos.y, camera.pos.z)
        if (dist < maxDistance * maxDistance) {
            matrices.push()
            matrices.translate(x + 0.5, y + 1.5, z + 0.5)
            matrices.multiply(camera.rotation)
            matrices.scale(-0.025f, -0.025f, +0.025f)

            val matrixModel = matrices.peek().model
            val opacity = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25f)
            val backgroundColor = color(0x000000, opacity)
            val textX = -textRenderer.getWidth(label) / 2f
            textRenderer.draw(label, textX, 0f, Colors.WHITE, false, matrixModel, vertexConsumers, false, backgroundColor, light)

            matrices.pop()
        }
    }

    companion object {
        private const val SELLING_ROTATION_MULTIPLIER = 1.2f
        // private const val PRICE_ROTATION_MULTIPLIER = -2.5f

        private const val OWNER_LABEL = "block.adorn.trading_station.label.owner"
        private const val SELLING_LABEL = "block.adorn.trading_station.label.selling"
        private const val PRICE_LABEL = "block.adorn.trading_station.label.price"
    }
}
