package juuxel.adorn.gui.widget

import io.github.cottonmc.cotton.gui.widget.WWidget
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment

class ArrowWidget : WWidget() {
    @Environment(EnvType.CLIENT)
    override fun paintBackground(x: Int, y: Int) {
        // TODO: Draw arrow here
    }

    override fun canResize() = true
}
