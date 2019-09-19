package juuxel.adorn.gui.widget

import io.github.cottonmc.cotton.gui.client.ScreenDrawing
import io.github.cottonmc.cotton.gui.widget.WPlainPanel
import io.github.cottonmc.cotton.gui.widget.WWidget
import juuxel.adorn.util.Colors
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.ingame.BookScreen
import net.minecraft.client.resource.language.I18n
import net.minecraft.client.sound.PositionedSoundInstance
import net.minecraft.sound.SoundEvents

class WPageControl(private val cardPanel: WCardPanel, private val textColor: Int, width: Int) : WPlainPanel() {
    init {
        add(WPageTurnButton(PageTurnDirection.Previous), 0, 0)
        add(WPageTurnButton(PageTurnDirection.Next), width * 18 - 23, 0)
    }

    override fun paintBackground(x: Int, y: Int, mouseX: Int, mouseY: Int) {
        super.paintBackground(x, y, mouseX, mouseY)
        val pageIndicator = I18n.translate("book.pageIndicator", cardPanel.selectedCard + 1, cardPanel.cardCount)
        val font = MinecraftClient.getInstance().textRenderer
        val indicatorWidth = font.getStringWidth(pageIndicator)
        ScreenDrawing.drawString(pageIndicator, x + width / 2 - indicatorWidth / 2, y + height / 2 - font.fontHeight / 2, textColor)
    }

    private enum class PageTurnDirection {
        Previous, Next
    }

    private inner class WPageTurnButton(private val direction: PageTurnDirection) : WWidget() {
        override fun canResize() = true
        override fun setSize(x: Int, y: Int) {
            super.setSize(23, y)
        }

        private fun isEnabled(): Boolean =
            when (direction) {
                PageTurnDirection.Previous -> cardPanel.hasPrevious()
                PageTurnDirection.Next -> cardPanel.hasNext()
            }

        override fun paintBackground(x: Int, y: Int, mouseX: Int, mouseY: Int) {
            val px = 1 / 256f
            val tx = if (isWithinBounds(mouseX, mouseY)) 23 else 0
            var ty = 192
            if (direction == PageTurnDirection.Previous) {
                ty += 13
            }

            ScreenDrawing.rect(BookScreen.BOOK_TEXTURE, x, y + height / 2 - 7, 23, 13, tx * px, ty * px, (tx + 23) * px, (ty + 13) * px, Colors.WHITE)
        }

        override fun onClick(x: Int, y: Int, button: Int) {
            if (!isEnabled()) return

            MinecraftClient.getInstance().soundManager.play(PositionedSoundInstance.master(SoundEvents.ITEM_BOOK_PAGE_TURN, 1f))
            when (direction) {
                PageTurnDirection.Previous -> cardPanel.selectPrevious()
                PageTurnDirection.Next -> cardPanel.selectNext()
            }
        }
    }
}
