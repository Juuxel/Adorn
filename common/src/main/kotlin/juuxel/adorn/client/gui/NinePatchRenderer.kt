package juuxel.adorn.client.gui

import com.mojang.blaze3d.systems.RenderSystem
import juuxel.libninepatch.ContextualTextureRenderer
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

object NinePatchRenderer : DrawableHelper(), ContextualTextureRenderer<Identifier, MatrixStack> {
    init {
        zOffset = 0
    }

    override fun draw(texture: Identifier, context: MatrixStack, x: Int, y: Int, width: Int, height: Int, u1: Float, v1: Float, u2: Float, v2: Float) {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        RenderSystem.setShaderTexture(0, texture)
        val positionMatrix = context.peek().positionMatrix
        drawTexturedQuad(positionMatrix, x, x + width, y, y + height, zOffset, u1, u2, v1, v2)
    }
}
