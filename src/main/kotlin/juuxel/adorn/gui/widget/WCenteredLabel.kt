package juuxel.adorn.gui.widget

import io.github.cottonmc.cotton.gui.client.ScreenDrawing
import io.github.cottonmc.cotton.gui.widget.WWidget
import net.minecraft.text.Text

class WCenteredLabel(private val text: Text, private val color: Int) : WWidget() {
    override fun paintBackground(x: Int, y: Int) {
        ScreenDrawing.drawCenteredWithShadow(
            text.asFormattedString(),
            x + width / 2, y + height / 2, color
        )
    }

    override fun canResize() = true
}
