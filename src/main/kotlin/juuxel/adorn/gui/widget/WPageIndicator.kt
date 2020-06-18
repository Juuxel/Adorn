package juuxel.adorn.gui.widget

import io.github.cottonmc.cotton.gui.client.ScreenDrawing
import io.github.cottonmc.cotton.gui.widget.WPlainPanel
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.resource.language.I18n
import net.minecraft.client.util.math.MatrixStack

// TODO: why is this still here
class WPageIndicator(private val pages: PageContainer, private val textColor: Int, private val alignment: HorizontalAlignment) : WPlainPanel() {
    @Environment(EnvType.CLIENT)
    override fun paint(matrices: MatrixStack, x: Int, y: Int, mouseX: Int, mouseY: Int) {
        super.paint(matrices, x, y, mouseX, mouseY)
        val pageIndicator = I18n.translate("book.pageIndicator", pages.currentPage + 1, pages.pageCount)
        val font = MinecraftClient.getInstance().textRenderer
        ScreenDrawing.drawString(matrices, pageIndicator, alignment, x, y + height / 2 - font.fontHeight / 2, width, textColor)
    }
}
