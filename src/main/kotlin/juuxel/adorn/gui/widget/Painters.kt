package juuxel.adorn.gui.widget

import io.github.cottonmc.cotton.gui.client.BackgroundPainter
import io.github.cottonmc.cotton.gui.client.ScreenDrawing
import io.github.cottonmc.cotton.gui.widget.WItemSlot

object Painters {
    /**
     * A background painter that paints LibGui-style slots.
     */
    val LIBGUI_STYLE_SLOT: BackgroundPainter = BackgroundPainter { left, top, panel ->
        if (panel !is WItemSlot) {
            ScreenDrawing.drawBeveledPanel(
                left - 1,
                top - 1,
                panel.width,
                panel.height,
                -0x48000000,
                0x4C000000,
                -0x47000001
            )
        } else {
            for (x in 0 until panel.width step 18) {
                for (y in 0 until panel.height step 18) {
                    val lo = ScreenDrawing.colorAtOpacity(0x000000, 0.2f)
                    val bg = ScreenDrawing.colorAtOpacity(0x000000, 0.2f / 2.4f)
                    val hi = ScreenDrawing.colorAtOpacity(0xFFFFFF, 0.2f)

                    if (panel.isBigSlot) {
                        ScreenDrawing.drawBeveledPanel(
                            x + left - 4, y + top - 4, 24, 24,
                            lo, bg, hi
                        )
                    } else {
                        ScreenDrawing.drawBeveledPanel(
                            x + left - 1, y + top - 1, 18, 18,
                            lo, bg, hi
                        )
                    }
                }
            }
        }
    }
}