package juuxel.adorn.client.gui

import net.minecraft.client.font.TextRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text

object TextRendering {
    fun drawCentered(matrices: MatrixStack, textRenderer: TextRenderer, text: Text, centerX: Int, y: Int, color: Int) {
        val width = textRenderer.getWidth(text)
        val x = centerX - width / 2
        textRenderer.draw(matrices, text, x.toFloat(), y.toFloat(), color)
    }
}
