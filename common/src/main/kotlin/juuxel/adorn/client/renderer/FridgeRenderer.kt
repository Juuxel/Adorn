package juuxel.adorn.client.renderer

import juuxel.adorn.AdornCommon
import juuxel.adorn.block.FridgeBlock
import juuxel.adorn.block.entity.FridgeBlockEntity
import net.minecraft.block.enums.DoorHinge
import net.minecraft.client.render.RenderLayers
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.BlockRenderManager
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.RotationAxis
import kotlin.math.pow

class FridgeRenderer(context: BlockEntityRendererFactory.Context) : BlockEntityRenderer<FridgeBlockEntity> {
    private val renderManager: BlockRenderManager = context.renderManager

    override fun render(entity: FridgeBlockEntity, tickDelta: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int, overlay: Int) {
        val state = entity.cachedState

        matrices.push()
        matrices.translate(0.5f, 0.5f, 0.5f)
        val facing = state[FridgeBlock.FACING]
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180f - facing.asRotation()))
        matrices.translate(-0.5f, -0.5f, -0.5f)

        val hinge = state[FridgeBlock.HINGE]
        val doorRotation = entity.getAnimationProgress(tickDelta)
        matrices.translate(0f, 0f, 3f/16f)
        if (hinge == DoorHinge.LEFT) {
            matrices.translate(1f, 0f, 0f)
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-getRotationDegrees(doorRotation)))
            matrices.translate(-1f, 0f, 0f)
        } else {
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(getRotationDegrees(doorRotation)))
        }
        matrices.translate(0f, 0f, -3f/16f)

        val matrixEntry = matrices.peek()
        val vertexConsumer = vertexConsumers.getBuffer(RenderLayers.getEntityBlockLayer(state, false))
        val model = renderManager.models.modelManager.getModel(getModelForHinge(hinge))
        renderManager.modelRenderer.render(matrixEntry, vertexConsumer, state, model, 1f, 1f, 1f, light, overlay)
        matrices.pop()
    }

    companion object {
        val LEFT_DOOR_MODEL = ModelIdentifier(AdornCommon.id("fridge_door_left_handle"), "")
        val RIGHT_DOOR_MODEL = ModelIdentifier(AdornCommon.id("fridge_door_right_handle"), "")

        private fun getModelForHinge(hinge: DoorHinge): ModelIdentifier =
            if (hinge == DoorHinge.RIGHT) LEFT_DOOR_MODEL else RIGHT_DOOR_MODEL

        private fun getRotationDegrees(progress: Float): Float {
            val modifiedProgress = 1 - (1 - progress).pow(3)
            return MathHelper.lerp(modifiedProgress, 0f, 55f)
        }
    }
}
