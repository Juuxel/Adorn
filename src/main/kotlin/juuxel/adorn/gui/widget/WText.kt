package juuxel.adorn.gui.widget

import io.github.cottonmc.cotton.gui.client.LibGuiClient
import io.github.cottonmc.cotton.gui.client.ScreenDrawing
import io.github.cottonmc.cotton.gui.widget.WLabel
import io.github.cottonmc.cotton.gui.widget.WWidget
import juuxel.adorn.mixin.ScreenAccessor
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.TextComponentUtil
import net.minecraft.text.ClickEvent
import net.minecraft.text.Text

/**
 * Like WLabel, but multiline.
 *
 * @property text the text
 * @property color the text color
 * @property pages an optional page container for click events
 */
class WText(private val text: Text, private val color: Int = WLabel.DEFAULT_TEXT_COLOR, private val pages: PageContainer? = null) : WWidget() {
    private var wrappedLines: List<Text> = listOf(text)

    override fun canResize() = true

    override fun setSize(x: Int, y: Int) {
        super.setSize(x, y)
        if (FabricLoader.getInstance().environmentType === EnvType.CLIENT) {
            wrapLines(x)
        }
    }

    @Environment(EnvType.CLIENT)
    private fun wrapLines(width: Int) {
        val font = MinecraftClient.getInstance().textRenderer
        wrappedLines = TextComponentUtil.wrapLines(text, width, font, true, true)
    }

    @Environment(EnvType.CLIENT)
    override fun paintBackground(x: Int, y: Int, mouseX: Int, mouseY: Int) {
        val font = MinecraftClient.getInstance().textRenderer
        for ((i, text) in wrappedLines.withIndex()) {
            val actualColor =
                if (color == WLabel.DEFAULT_TEXT_COLOR && LibGuiClient.config.darkMode)
                    WLabel.DEFAULT_DARKMODE_TEXT_COLOR
                else
                    color

            ScreenDrawing.drawString(text.asFormattedString(), x, y + i * font.fontHeight, actualColor)
        }

        val text = getTextAt(mouseX, mouseY)
        (MinecraftClient.getInstance().currentScreen as? ScreenAccessor)
            ?.callRenderComponentHoverEffect(text, x + mouseX, y + mouseY)
    }

    override fun onClick(x: Int, y: Int, button: Int) {
        getTextAt(x, y)?.let { text ->
            text.style?.clickEvent?.let { clickEvent ->
                if (clickEvent.action === ClickEvent.Action.CHANGE_PAGE) {
                    if (pages != null) {
                        val page = clickEvent.value.toIntOrNull() ?: return
                        val pageIndex = page - 1
                        if (pageIndex >= 0 && pageIndex < pages.pageCount) {
                            pages.currentPage = pageIndex
                        }
                    }
                } else {
                    val result = MinecraftClient.getInstance().currentScreen?.handleComponentClicked(text) ?: false
                    if (result && clickEvent.action === ClickEvent.Action.RUN_COMMAND) {
                        MinecraftClient.getInstance().openScreen(null)
                    }
                }
            }
        }
    }

    private fun getTextAt(x: Int, y: Int): Text? {
        val font = MinecraftClient.getInstance().textRenderer
        val line = y / font.fontHeight

        if (line >= 0 && line < wrappedLines.size) {
            val lineText = wrappedLines[line]
            var xi = 0
            for (component in lineText) {
                xi += font.getStringWidth(component.asFormattedString())
                if (xi > x) return component
            }
        }

        return null
    }
}
