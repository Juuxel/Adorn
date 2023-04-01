package juuxel.adorn.client.gui.widget

import juuxel.adorn.util.Colors
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text

class ConfigScreenHeading(
    private val text: Text,
    private val x: Int,
    private val y: Int,
    private val width: Int
) : DrawableHelper(), Drawable {
    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        val client = MinecraftClient.getInstance()
        val textRenderer = client.textRenderer
        val textWidth = textRenderer.getWidth(text)
        val lineWidth = width / 2 - OUTER_GAP_WIDTH - INNER_GAP_WIDTH - textWidth / 2
        val lineY = HEIGHT / 2 - 2

        matrices.push()
        matrices.translate(x.toDouble(), y.toDouble(), 0.0)

        if (textWidth <= width - 2 * OUTER_GAP_WIDTH - 2 * INNER_GAP_WIDTH) {
            // Left line
            matrices.push()
            matrices.translate(OUTER_GAP_WIDTH.toDouble(), 0.0, 0.0)
            fill(matrices, 0, lineY, lineWidth, lineY + 1, Colors.WHITE)
            matrices.pop()

            // Right line
            matrices.push()
            matrices.translate(width - lineWidth - OUTER_GAP_WIDTH.toDouble(), 0.0, 0.0)
            fill(matrices, 0, lineY, lineWidth, lineY + 1, Colors.WHITE)
            matrices.pop()
        }

        // Label
        val textX = (width - textWidth) * 0.5f
        val textY = (HEIGHT - textRenderer.fontHeight) / 2
        textRenderer.draw(matrices, text, textX, textY.toFloat(), Colors.WHITE)
        matrices.pop()
    }

    companion object {
        const val HEIGHT = 18
        private const val OUTER_GAP_WIDTH = 5
        private const val INNER_GAP_WIDTH = 5
    }
}
