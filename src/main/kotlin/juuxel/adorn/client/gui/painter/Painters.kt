package juuxel.adorn.client.gui.painter

import io.github.cottonmc.cotton.gui.client.BackgroundPainter
import io.github.cottonmc.cotton.gui.client.ScreenDrawing
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import juuxel.adorn.client.resources.ColorManager
import juuxel.adorn.util.Colors
import kotlin.math.max
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.gui.screen.ingame.BookScreen
import net.minecraft.util.Identifier

@Environment(EnvType.CLIENT)
object Painters {
    /**
     * A background painter that paints LibGui-style slots.
     */
    val LIBGUI_STYLE_SLOT: BackgroundPainter = BackgroundPainter { left, top, panel ->
        // TODO: Add focus frames
        for (x in 0 until panel.width step 18) {
            for (y in 0 until panel.height step 18) {
                val lo = ScreenDrawing.colorAtOpacity(0x000000, 0.2f)
                val bg = ScreenDrawing.colorAtOpacity(0x000000, 0.2f / 2.4f)
                val hi = ScreenDrawing.colorAtOpacity(0xFFFFFF, 0.2f)

                if (panel is WItemSlot && panel.isBigSlot) {
                    ScreenDrawing.drawBeveledPanel(
                        x + left - 3, y + top - 3, 24, 24,
                        lo, bg, hi
                    )
                } else {
                    ScreenDrawing.drawBeveledPanel(
                        x + left, y + top, 18, 18,
                        lo, bg, hi
                    )
                }
            }
        }
    }

    /**
     * A painter that paints book backgrounds.
     */
    val BOOK: BackgroundPainter = BackgroundPainter { x, y, widget ->
        val px = 1 / 256f
        ScreenDrawing.texturedRect(
            x + (max(widget.width, 192) - 192) / 2, y + 2,
            192, 192,
            BookScreen.BOOK_TEXTURE,
            0f, 0f, 192 * px, 192 * px,
            Colors.WHITE
        )
    }

    fun palette(palette: Identifier, key: Identifier): BackgroundPainter =
        PaletteBackgroundPainter(ColorManager.getColors(palette), key)
}
