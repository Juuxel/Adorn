package juuxel.adorn.gui.widget

import io.github.cottonmc.cotton.gui.client.ScreenDrawing
import io.github.cottonmc.cotton.gui.widget.WPlainPanel
import io.github.cottonmc.cotton.gui.widget.data.Alignment
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.resource.language.I18n

class WPageIndicator(private val pages: PageContainer, private val textColor: Int, private val alignment: Alignment) : WPlainPanel() {
    @Environment(EnvType.CLIENT)
    override fun paintBackground(x: Int, y: Int, mouseX: Int, mouseY: Int) {
        super.paintBackground(x, y, mouseX, mouseY)
        val pageIndicator = I18n.translate("book.pageIndicator", pages.currentPage + 1, pages.pageCount)
        val font = MinecraftClient.getInstance().textRenderer
        ScreenDrawing.drawString(pageIndicator, alignment, x, y + height / 2 - font.fontHeight / 2, width, textColor)
    }
}
