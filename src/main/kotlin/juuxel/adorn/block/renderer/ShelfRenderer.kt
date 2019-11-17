package juuxel.adorn.block.renderer

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import juuxel.adorn.block.ShelfBlock
import juuxel.adorn.block.entity.ShelfBlockEntity
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Direction

@Environment(EnvType.CLIENT)
class ShelfRenderer(dispatcher: BlockEntityRenderDispatcher) : BlockEntityRenderer<ShelfBlockEntity>(dispatcher) {
    override fun render(
        be: ShelfBlockEntity, tickDelta: Float,
        matrixStack: MatrixStack, vertexConsumerProvider: VertexConsumerProvider, i: Int, j: Int
    ) {
        val facing = be.cachedState[ShelfBlock.FACING]

        // For first item
        val tx1 = when (facing) {
            Direction.SOUTH, Direction.WEST -> 12f / 16f
            else -> 4f / 16f
        }
        val tz1 = when (facing) {
            Direction.NORTH, Direction.WEST -> 12 / 16f
            else -> 4 / 16f
        }

        // For second item
        val tx2 = when (facing) {
            Direction.NORTH, Direction.WEST -> 12f / 16f
            else -> 4f / 16f
        }
        val tz2 = when (facing) {
            Direction.NORTH, Direction.EAST -> 12 / 16f
            else -> 4 / 16f
        }

        RenderSystem.pushMatrix()
        //RenderSystem.translated(x, y + /*7.1f*/ 9.6f / 16f, z)
        val itemRenderer = MinecraftClient.getInstance().itemRenderer

        RenderSystem.pushMatrix()
        RenderSystem.translatef(tx1, 0f, tz1)
        RenderSystem.scalef(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE)
        RenderSystem.rotatef(ITEM_1_Y_ROT + facing.asRotation(), 0f, 1f, 0f)
        //GlStateManager.rotatef(ITEM_X_ROT, 1f, 0f, 0f)
        itemRenderer.method_23178(be.getInvStack(0), ModelTransformation.Type.FIXED, i, j, matrixStack, vertexConsumerProvider)
        RenderSystem.popMatrix()

        RenderSystem.translatef(tx2, 0f, tz2)
        RenderSystem.scalef(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE)
        RenderSystem.rotatef(ITEM_2_Y_ROT + facing.asRotation(), 0f, 1f, 0f)
        //GlStateManager.rotatef(ITEM_X_ROT, 1f, 0f, 0f)
        itemRenderer.method_23178(be.getInvStack(1), ModelTransformation.Type.FIXED, i, j, matrixStack, vertexConsumerProvider)

        GlStateManager.popMatrix()
    }

    companion object {
        private const val ITEM_SCALE: Float = 0.5f
        //private const val ITEM_X_ROT: Float = 90f
        private const val ITEM_1_Y_ROT: Float = 10f
        private const val ITEM_2_Y_ROT: Float = -17f
    }
}
