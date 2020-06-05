package juuxel.adorn.gui.widget

import io.github.cottonmc.cotton.gui.client.ScreenDrawing
import io.github.cottonmc.cotton.gui.widget.WLabel
import io.github.cottonmc.cotton.gui.widget.WWidget
import io.github.cottonmc.cotton.gui.widget.data.Alignment
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text

/**
 * A vertically and horizontally centered label.
 *
 * @property text the text
 * @property color the text color
 */
class WCenteredLabel(private val text: Text, private val color: Int = WLabel.DEFAULT_TEXT_COLOR) : WWidget() {
    @Environment(EnvType.CLIENT)
    override fun paint(matrices: MatrixStack, x: Int, y: Int, mouseX: Int, mouseY: Int) {
        ScreenDrawing.drawString(matrices, text, Alignment.CENTER, x, y + height / 2 - 3, width, color)
    }

    override fun canResize() = true
}
