package juuxel.adorn.gui.widget

import io.github.cottonmc.cotton.gui.client.LibGuiClient
import io.github.cottonmc.cotton.gui.client.ScreenDrawing
import io.github.cottonmc.cotton.gui.widget.WLabel
import io.github.cottonmc.cotton.gui.widget.WWidget
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text

/**
 * Like WLabel, but multiline.
 */
class WText(var text: Text, val color: Int = WLabel.DEFAULT_TEXT_COLOR) : WWidget() {
    override fun canResize() = true

    override fun paintBackground(x: Int, y: Int, mouseX: Int, mouseY: Int) {
        val textRenderer = MinecraftClient.getInstance().textRenderer
        for ((i, str) in textRenderer.wrapStringToWidthAsList(text.asFormattedString(), width).withIndex()) {
            val actualColor =
                if (color == WLabel.DEFAULT_TEXT_COLOR && LibGuiClient.config.darkMode)
                    WLabel.DEFAULT_DARKMODE_TEXT_COLOR
                else
                    color

            ScreenDrawing.drawString(str, x, y + i * textRenderer.fontHeight, actualColor)
        }
    }
}
