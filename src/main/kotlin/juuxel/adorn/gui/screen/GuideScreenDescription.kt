package juuxel.adorn.gui.screen

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription
import io.github.cottonmc.cotton.gui.client.ScreenDrawing
import io.github.cottonmc.cotton.gui.widget.WLabel
import io.github.cottonmc.cotton.gui.widget.WPlainPanel
import io.github.cottonmc.cotton.gui.widget.WWidget
import juuxel.adorn.Adorn
import juuxel.adorn.gui.painter.Painters
import juuxel.adorn.gui.widget.*
import juuxel.adorn.guide.Guide
import juuxel.adorn.guide.Topic
import juuxel.adorn.util.Colors
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.sound.PositionedSoundInstance
import net.minecraft.sound.SoundEvents
import net.minecraft.text.TranslatableText

@Environment(EnvType.CLIENT)
class GuideScreenDescription(guide: Guide) : LightweightGuiDescription() {
    init {
        val root = WPlainPanel()
        val pageCards = WCardPanel()
        val prev = WPageTurnButton(pageCards, WPageTurnButton.Direction.Previous)
        val next = WPageTurnButton(pageCards, WPageTurnButton.Direction.Next)
        val indicator = WPageIndicator(pageCards, Colors.BLACK, Alignment.CENTER)

        pageCards.addCard(createTitlePage(guide))
        for (topic in guide.topics) pageCards.addCard(createPage(topic))

        root.add(pageCards, 35, 14, 116, 145)
        root.add(indicator, 34, 159, 113, 13)
        root.add(prev, 34, 159, 23, 13)
        root.add(next, 120, 159, 23, 13)
        root.add(WCloseButton(), 142, 14)
        root.setSize(192, 192)
        root.backgroundPainter = Painters.BOOK
        rootPanel = root
    }

    override fun addPainters() {}

    private fun createTitlePage(guide: Guide): WWidget {
        val result = WPlainPanel()
        result.add(WBigLabel(guide.title, WLabel.DEFAULT_TEXT_COLOR), 0, 25, 116, 20)
        result.add(WCenteredLabel(guide.subtitle, WLabel.DEFAULT_TEXT_COLOR), 0, 45, 116, 20)
        result.add(WCenteredLabel(TranslatableText("book.byAuthor", guide.author), WLabel.DEFAULT_TEXT_COLOR), 0, 60, 116, 20)
        return result
    }

    private fun createPage(topic: Topic): WWidget = WPlainPanel().apply {
        add(WItem(topic.icon), 0, 0)
        add(WCenteredLabel(topic.title.styled { it.isBold = true }, WLabel.DEFAULT_TEXT_COLOR), 0, 0, 116, 20)
        add(WText(topic.text), 4, 24, 116 - 4, 145 - 24)
    }

    private class WCloseButton : WWidget() {
        init {
            super.setSize(8, 8)
        }

        override fun setSize(x: Int, y: Int) {}

        override fun canResize() = false

        override fun paintBackground(x: Int, y: Int, mouseX: Int, mouseY: Int) {
            val texture = if (isWithinBounds(mouseX, mouseY)) ACTIVE_TEXTURE else INACTIVE_TEXTURE
            ScreenDrawing.rect(texture, x, y, 8, 8, Colors.WHITE)
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
            private val INACTIVE_TEXTURE = Adorn.id("textures/gui/close_book_inactive.png")
            private val ACTIVE_TEXTURE = Adorn.id("textures/gui/close_book_active.png")
        }
    }
}
