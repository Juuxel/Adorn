package juuxel.adorn.compat.modmenu

import io.github.cottonmc.cotton.gui.client.ScreenDrawing
import io.github.cottonmc.cotton.gui.widget.WWidget
import net.minecraft.client.gui.widget.AbstractButtonWidget
import net.minecraft.text.Text
import kotlin.math.min

class WDisabledButton(private val label: Text) : WWidget() {
    override fun canResize() = true

    override fun paintForeground(x: Int, y: Int, mouseX: Int, mouseY: Int) {
        val px = 1 / 256f

        val buttonLeft = 0*px
        val buttonTop = 46*px
        val halfWidth = min(width / 2, 198)
        val buttonWidth = halfWidth * px
        val buttonHeight = 20*px
        val buttonEndLeft = (200 - width / 2) * px

        ScreenDrawing.rect(
            AbstractButtonWidget.WIDGETS_LOCATION, x, y, width / 2, 20,
            buttonLeft, buttonTop,
            buttonLeft + buttonWidth, buttonTop + buttonHeight,
            -1
        )
        ScreenDrawing.rect(
            AbstractButtonWidget.WIDGETS_LOCATION, x + width / 2, y, width / 2, 20,
            buttonEndLeft, buttonTop,
            200 * px, buttonTop + buttonHeight,
            -1
        )

        ScreenDrawing.drawCenteredWithShadow(label.asFormattedString(), x + width / 2, y + (20 - 8) / 2, 0xA0A0A0)

        super.paintForeground(x, y, mouseX, mouseY)
    }
}