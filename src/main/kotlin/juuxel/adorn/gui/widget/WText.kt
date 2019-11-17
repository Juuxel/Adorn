package juuxel.adorn.gui.widget

import io.github.cottonmc.cotton.gui.client.LibGuiClient
import io.github.cottonmc.cotton.gui.client.ScreenDrawing
import io.github.cottonmc.cotton.gui.widget.WLabel
import io.github.cottonmc.cotton.gui.widget.WWidget
import io.github.cottonmc.cotton.gui.widget.data.Alignment
import juuxel.adorn.mixin.ScreenAccessor
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.sound.PositionedSoundInstance
import net.minecraft.client.util.Texts
import net.minecraft.sound.SoundEvents
import net.minecraft.text.ClickEvent
import net.minecraft.text.Text

/**
 * Like WLabel, but multiline.
 *
 * @property text the text
 * @property color the text color
 * @property pages an optional page container for click events
 * @property alignment the horizontal text alignment
 * @property centerVertically true if the text is vertically centered (TODO: A vertical alignment?)
 */
class WText(
    private val text: Text,
    private val color: Int = WLabel.DEFAULT_TEXT_COLOR,
    private val pages: PageContainer? = null,
    private val alignment: Alignment = Alignment.LEFT,
    private val centerVertically: Boolean = false
) : WWidget() {
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
        wrappedLines = Texts.wrapLines(text, width, font, true, true)
    }

    @Environment(EnvType.CLIENT)
    override fun paintBackground(x: Int, y: Int, mouseX: Int, mouseY: Int) {
        val font = MinecraftClient.getInstance().textRenderer
        val yOffset = if (centerVertically) height / 2 - wrappedLines.size * font.fontHeight / 2 else 0
        for ((i, text) in wrappedLines.withIndex()) {
            val actualColor =
                if (color == WLabel.DEFAULT_TEXT_COLOR && LibGuiClient.config.darkMode)
                    WLabel.DEFAULT_DARKMODE_TEXT_COLOR
                else
                    color

            val str = text.asFormattedString()
            ScreenDrawing.drawString(str, alignment, x, y + yOffset, width, actualColor)
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
                            MinecraftClient.getInstance().soundManager.play(
                                PositionedSoundInstance.master(SoundEvents.ITEM_BOOK_PAGE_TURN, 1f)
                            )
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
