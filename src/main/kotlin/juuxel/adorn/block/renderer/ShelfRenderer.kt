package juuxel.adorn.block.renderer

import com.mojang.blaze3d.platform.GlStateManager
import juuxel.adorn.block.ShelfBlock
import juuxel.adorn.block.entity.ShelfBlockEntity
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.util.math.Direction

@Environment(EnvType.CLIENT)
class ShelfRenderer : BlockEntityRenderer<ShelfBlockEntity>() {
    override fun render(be: ShelfBlockEntity, x: Double, y: Double, z: Double, tickDelta: Float, i: Int) {
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

        GlStateManager.pushMatrix()
        GlStateManager.translated(x, y + /*7.1f*/ 9.6f / 16f, z)
        val itemRenderer = MinecraftClient.getInstance().itemRenderer

        GlStateManager.pushMatrix()
        GlStateManager.translatef(tx1, 0f, tz1)
        GlStateManager.scalef(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE)
        GlStateManager.rotatef(ITEM_1_Y_ROT, 0f, 1f, 0f)
        GlStateManager.rotatef(ITEM_X_ROT, 1f, 0f, 0f)
        itemRenderer.renderItem(be.getInvStack(0), ModelTransformation.Type.FIXED)
        GlStateManager.popMatrix()

        GlStateManager.translatef(tx2, 0f, tz2)
        GlStateManager.scalef(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE)
        GlStateManager.rotatef(ITEM_2_Y_ROT, 0f, 1f, 0f)
        GlStateManager.rotatef(ITEM_X_ROT, 1f, 0f, 0f)
        itemRenderer.renderItem(be.getInvStack(1), ModelTransformation.Type.FIXED)

        GlStateManager.popMatrix()
    }

    companion object {
        private const val ITEM_SCALE: Float = 0.5f
        private const val ITEM_X_ROT: Float = 0f
        private const val ITEM_1_Y_ROT: Float = 10f
        private const val ITEM_2_Y_ROT: Float = -17f
    }
}
