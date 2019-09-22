package juuxel.adorn.gui.widget

import io.github.cottonmc.cotton.gui.client.ScreenDrawing
import io.github.cottonmc.cotton.gui.widget.WPlainPanel
import net.minecraft.client.MinecraftClient
import net.minecraft.client.resource.language.I18n

class WPageIndicator(private val pages: PageContainer, private val textColor: Int, private val alignment: Alignment) : WPlainPanel() {
    override fun paintBackground(x: Int, y: Int, mouseX: Int, mouseY: Int) {
        super.paintBackground(x, y, mouseX, mouseY)
        val pageIndicator = I18n.translate("book.pageIndicator", pages.currentPage + 1, pages.pageCount)
        val font = MinecraftClient.getInstance().textRenderer
        val indicatorWidth = font.getStringWidth(pageIndicator)
        val tx = when (alignment) {
            Alignment.LEFT -> x
            Alignment.CENTER -> x + width / 2 - indicatorWidth / 2
            Alignment.RIGHT -> x + width - indicatorWidth
        }
        ScreenDrawing.drawString(pageIndicator, tx, y + height / 2 - font.fontHeight / 2, textColor)
    }
}
