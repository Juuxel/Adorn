package juuxel.adorn.client.gui.painter

import io.github.cottonmc.cotton.gui.client.BackgroundPainter
import io.github.cottonmc.cotton.gui.client.ScreenDrawing
import io.github.cottonmc.cotton.gui.widget.WWidget
import juuxel.adorn.client.resources.ColorPalette
import juuxel.adorn.util.color
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

@Environment(EnvType.CLIENT)
class PaletteBackgroundPainter(private val palette: ColorPalette, private val key: Identifier) : BackgroundPainter {
    private val panelColor by lazy {
        color(palette[key].bg)
    }

    override fun paintBackground(matrices: MatrixStack, left: Int, top: Int, panel: WWidget) {
        ScreenDrawing.drawGuiPanel(matrices, left, top, panel.width, panel.height, panelColor)
    }
}
