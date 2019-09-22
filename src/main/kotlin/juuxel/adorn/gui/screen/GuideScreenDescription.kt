package juuxel.adorn.gui.screen

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription
import io.github.cottonmc.cotton.gui.widget.WPlainPanel
import juuxel.adorn.gui.painter.Painters
import juuxel.adorn.gui.widget.Alignment
import juuxel.adorn.gui.widget.WCardPanel
import juuxel.adorn.gui.widget.WPageIndicator
import juuxel.adorn.gui.widget.WPageTurnButton
import juuxel.adorn.util.Colors
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment

@Environment(EnvType.CLIENT)
class GuideScreenDescription : LightweightGuiDescription() {
    init {
        val root = WPlainPanel()
        val pages = WCardPanel()
        val prev = WPageTurnButton(pages, WPageTurnButton.Direction.Previous)
        val next = WPageTurnButton(pages, WPageTurnButton.Direction.Next)
        val indicator = WPageIndicator(pages, Colors.WHITE, Alignment.RIGHT)

        root.backgroundPainter = Painters.BOOK
    }

    override fun addPainters() {}
}
