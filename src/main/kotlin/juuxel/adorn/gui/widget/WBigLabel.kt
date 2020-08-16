package juuxel.adorn.gui.widget

import io.github.cottonmc.cotton.gui.client.ScreenDrawing
import io.github.cottonmc.cotton.gui.widget.WWidget
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text

class WBigLabel(private val text: Text, private val color: Int, private val scale: Float = DEFAULT_SCALE) : WWidget() {
    @Environment(EnvType.CLIENT)
    override fun paint(matrices: MatrixStack, x: Int, y: Int, mouseX: Int, mouseY: Int) {
        matrices.push()
        matrices.translate((x + width / 2).toDouble(), (y + height / 2 - 3).toDouble(), 0.0)
        matrices.scale(scale, scale, 1.0f)
        val font = MinecraftClient.getInstance().textRenderer
        ScreenDrawing.drawString(matrices, text.asOrderedText(), -(font.getWidth(text) / 2), 0, color)
        matrices.pop()
    }

    override fun canResize() = true

    companion object {
        const val DEFAULT_SCALE = 1.5f
    }
}
