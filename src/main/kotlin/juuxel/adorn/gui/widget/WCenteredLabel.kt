package juuxel.adorn.gui.widget

import io.github.cottonmc.cotton.gui.client.ScreenDrawing
import io.github.cottonmc.cotton.gui.widget.WWidget
import io.github.cottonmc.cotton.gui.widget.data.Alignment
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.text.Text

/**
 * A vertically and horizontally centered label.
 *
 * @property text the text
 * @property color the text color
 */
class WCenteredLabel(private val text: Text, private val color: Int) : WWidget() {
    @Environment(EnvType.CLIENT)
    override fun paintBackground(x: Int, y: Int) {
        ScreenDrawing.drawString(text.asFormattedString(), Alignment.CENTER, x, y + height / 2 - 3, width, color)
    }

    override fun canResize() = true
}
