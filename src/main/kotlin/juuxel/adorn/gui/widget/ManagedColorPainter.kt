package juuxel.adorn.gui.widget

import io.github.cottonmc.cotton.gui.client.BackgroundPainter
import io.github.cottonmc.cotton.gui.client.ScreenDrawing
import io.github.cottonmc.cotton.gui.widget.WWidget
import juuxel.adorn.resources.ColorMap
import juuxel.adorn.util.color
import net.minecraft.util.Identifier

class ManagedColorPainter(private val colorMap: ColorMap, private val key: Identifier) : BackgroundPainter {
    private val panelColor by lazy {
        color(colorMap.getValue(key).bg)
    }

    override fun paintBackground(left: Int, top: Int, panel: WWidget) {
        ScreenDrawing.drawGuiPanel(left - 8, top - 8, panel.width + 16, panel.height + 16, panelColor)
    }
}
