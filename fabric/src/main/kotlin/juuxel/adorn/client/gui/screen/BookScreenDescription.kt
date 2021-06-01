package juuxel.adorn.client.gui.screen

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription
import io.github.cottonmc.cotton.gui.client.ScreenDrawing
import io.github.cottonmc.cotton.gui.widget.WItem
import io.github.cottonmc.cotton.gui.widget.WLabel
import io.github.cottonmc.cotton.gui.widget.WPlainPanel
import io.github.cottonmc.cotton.gui.widget.WWidget
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment
import juuxel.adorn.AdornCommon
import juuxel.adorn.client.book.Book
import juuxel.adorn.client.book.Page
import juuxel.adorn.client.gui.painter.Painters
import juuxel.adorn.menu.widget.PageContainer
import juuxel.adorn.menu.widget.WBigLabel
import juuxel.adorn.menu.widget.WBookCardPanel
import juuxel.adorn.menu.widget.WBookText
import juuxel.adorn.menu.widget.WPageTurnButton
import juuxel.adorn.util.Colors
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.sound.PositionedSoundInstance
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.sound.SoundEvents
import net.minecraft.text.TranslatableText

@Environment(EnvType.CLIENT)
class BookScreenDescription(book: Book) : LightweightGuiDescription() {
    init {
        val root = WPlainPanel()
        val pageCards = WBookCardPanel()
        val prev = WPageTurnButton(pageCards, WPageTurnButton.Direction.PREVIOUS)
        val next = WPageTurnButton(pageCards, WPageTurnButton.Direction.NEXT)

        pageCards.add(createTitlePage(book))
        for (topic in book.pages) pageCards.add(createPageWidget(pageCards, topic))

        root.add(prev, 49, 159, 23, 13)
        root.add(next, 116, 159, 23, 13)
        root.add(pageCards, 35, 14, 116, 145)
        root.add(WCloseButton(), 142, 14)
        root.setSize(192, 192)
        root.backgroundPainter = Painters.BOOK
        rootPanel = root
        root.validate(this)
    }

    override fun addPainters() {}

    private fun createTitlePage(book: Book): WWidget {
        val result = WPlainPanel()
        result.add(WBigLabel(book.title, WLabel.DEFAULT_TEXT_COLOR, scale = book.titleScale), 0, 25, 116, 20)
        result.add(WLabel(book.subtitle, WLabel.DEFAULT_TEXT_COLOR).setHorizontalAlignment(HorizontalAlignment.CENTER).disableDarkmode(), 0, 45, 116, 20)
        result.add(WLabel(TranslatableText("book.byAuthor", book.author), WLabel.DEFAULT_TEXT_COLOR).setHorizontalAlignment(HorizontalAlignment.CENTER).disableDarkmode(), 0, 60, 116, 20)
        return result
    }

    private fun createPageWidget(pages: PageContainer, page: Page): WWidget = WPlainPanel().apply {
        val item = WItem(page.icons.map { it.defaultStack })
        add(item, 0, 0)
        add(
            WBookText(
                page.title.copy().styled { it.withBold(true) }
            ).setVerticalAlignment(VerticalAlignment.CENTER),
            20, 0, 116 - 40, 20
        )
        add(WBookText(page.text, pages = pages), 4, 24, 116 - 4, 145 - 24)
    }

    private class WCloseButton : WWidget() {
        init {
            super.setSize(8, 8)
        }

        override fun setSize(x: Int, y: Int) {}

        override fun canResize() = false

        override fun paint(matrices: MatrixStack, x: Int, y: Int, mouseX: Int, mouseY: Int) {
            val texture = if (isWithinBounds(mouseX, mouseY)) ACTIVE_TEXTURE else INACTIVE_TEXTURE
            ScreenDrawing.texturedRect(x, y, 8, 8, texture, Colors.WHITE)
        }

        override fun onClick(x: Int, y: Int, button: Int) {
            if (isWithinBounds(x, y)) {
                val client = MinecraftClient.getInstance()
                client.soundManager.play(
                    PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f)
                )
                client.openScreen(null)
            }
        }

        companion object {
            private val INACTIVE_TEXTURE = AdornCommon.id("textures/gui/close_book_inactive.png")
            private val ACTIVE_TEXTURE = AdornCommon.id("textures/gui/close_book_active.png")
        }
    }
}
