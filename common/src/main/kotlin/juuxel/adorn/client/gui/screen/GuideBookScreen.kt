package juuxel.adorn.client.gui.screen

import com.mojang.blaze3d.systems.RenderSystem
import juuxel.adorn.AdornCommon
import juuxel.adorn.client.book.Book
import juuxel.adorn.client.book.Image
import juuxel.adorn.client.book.Page
import juuxel.adorn.client.gui.Scissors
import juuxel.adorn.client.gui.forEach
import juuxel.adorn.client.gui.widget.Draggable
import juuxel.adorn.client.gui.widget.FlipBook
import juuxel.adorn.client.gui.widget.Panel
import juuxel.adorn.client.gui.widget.ScrollEnvelope
import juuxel.adorn.client.gui.widget.SizedElement
import juuxel.adorn.client.gui.widget.TickingElement
import juuxel.adorn.util.Colors
import juuxel.adorn.util.animation.AnimationEngine
import juuxel.adorn.util.color
import juuxel.adorn.util.interleave
import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.ingame.BookScreen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.PageTurnWidget
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.sound.PositionedSoundInstance
import net.minecraft.client.util.NarratorManager
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundEvents
import net.minecraft.text.ClickEvent
import net.minecraft.text.Style
import net.minecraft.text.Text
import kotlin.math.max

class GuideBookScreen(private val book: Book) : Screen(NarratorManager.EMPTY) {
    private lateinit var flipBook: FlipBook
    private lateinit var previousPageButton: PageTurnWidget
    private lateinit var nextPageButton: PageTurnWidget
    private val animationEngine = AnimationEngine()

    override fun init() {
        val x = (width - BOOK_SIZE) / 2
        val y = (height - BOOK_SIZE) / 2
        val pageX = x + 35
        val pageY = y + 14

        addDrawableChild(CloseButton(x + 142, y + 14) { close() })
        previousPageButton = addDrawableChild(PageTurnWidget(x + 49, y + 159, false, { flipBook.showPreviousPage() }, true))
        nextPageButton = addDrawableChild(PageTurnWidget(x + 116, y + 159, true, { flipBook.showNextPage() }, true))

        // The flip book has to be added last so that
        // its mouse hover tooltip renders on top of all widgets.
        flipBook = addDrawableChild(FlipBook(this::updatePageTurnButtons))
        flipBook.add(TitlePage(pageX, pageY, book))
        for (page in book.pages) {
            val panel = Panel()
            panel.add(BookPageTitle(pageX, pageY, page))
            val body = BookPageBody(pageX, pageY + PAGE_TEXT_Y, page)
            panel.add(ScrollEnvelope(pageX, pageY + PAGE_TEXT_Y, PAGE_WIDTH, PAGE_BODY_HEIGHT, body, animationEngine))
            flipBook.add(panel)
        }

        updatePageTurnButtons()
        animationEngine.start()
    }

    private fun updatePageTurnButtons() {
        previousPageButton.visible = flipBook.hasPreviousPage()
        nextPageButton.visible = flipBook.hasNextPage()
    }

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        renderBackground(matrices)
        RenderSystem.setShader(GameRenderer::getPositionTexShader)
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        RenderSystem.setShaderTexture(0, BookScreen.BOOK_TEXTURE)
        val x = (width - BOOK_SIZE) / 2
        val y = (height - BOOK_SIZE) / 2
        drawTexture(matrices, x, y, 0, 0, BOOK_SIZE, BOOK_SIZE)
        super.render(matrices, mouseX, mouseY, delta)
    }

    override fun handleTextClick(style: Style?): Boolean {
        if (style != null) {
            val clickEvent = style.clickEvent

            if (clickEvent != null && clickEvent.action == ClickEvent.Action.CHANGE_PAGE) {
                val page = clickEvent.value.toIntOrNull() ?: return false
                val pageIndex = page - 1 // 1-indexed => 0-indexed

                if (pageIndex in 0 until flipBook.pageCount) {
                    flipBook.currentPage = pageIndex
                    client!!.soundManager.play(PositionedSoundInstance.master(SoundEvents.ITEM_BOOK_PAGE_TURN, 1f))
                    return true
                }
            }
        }

        return super.handleTextClick(style)
    }

    override fun tick() {
        forEach<TickingElement> {
            it.tick()
        }

        val focused = this.focused
        if (focused is Draggable && !isDragging) {
            focused.stopDragging()
        }
    }

    override fun removed() {
        animationEngine.stop()
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        val currentPage = flipBook.currentPageValue
        if (currentPage is Panel) {
            val scroll = currentPage.children().firstOrNull { it is ScrollEnvelope }
            if (scroll != null) {
                return scroll.keyPressed(keyCode, scanCode, modifiers)
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    companion object {
        private const val BOOK_SIZE = 192
        private const val PAGE_TITLE_X = 20
        private const val PAGE_WIDTH = 116
        private const val PAGE_BODY_HEIGHT = 121
        private const val PAGE_TITLE_WIDTH = PAGE_WIDTH - 2 * PAGE_TITLE_X
        private const val PAGE_TEXT_X = 4
        private const val PAGE_TEXT_Y = 24
        private const val PAGE_IMAGE_GAP = 4
        private const val ICON_DURATION = 25
        private val CLOSE_BOOK_ACTIVE_TEXTURE = AdornCommon.id("textures/gui/close_book_active.png")
        private val CLOSE_BOOK_INACTIVE_TEXTURE = AdornCommon.id("textures/gui/close_book_inactive.png")
        private val HOVER_AREA_HIGHLIGHT_COLOR = color(0xFFFFFF, alpha = 0x80)
    }

    private inner class TitlePage(private val x: Int, private val y: Int, private val book: Book) : Element, Drawable {
        private val byAuthor = Text.translatable("book.byAuthor", book.author)

        override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
            val cx = x + PAGE_WIDTH / 2

            matrices.push()
            matrices.translate(cx.toDouble(), (y + 7).toDouble() + 25, 0.0)
            matrices.scale(book.titleScale, book.titleScale, 1.0f)
            textRenderer.draw(matrices, book.title, -(textRenderer.getWidth(book.title) / 2).toFloat(), 0f, Colors.SCREEN_TEXT)
            matrices.pop()

            textRenderer.draw(matrices, book.subtitle, (cx - textRenderer.getWidth(book.subtitle) / 2).toFloat(), y + 45f, Colors.SCREEN_TEXT)
            textRenderer.draw(matrices, byAuthor, (cx - textRenderer.getWidth(byAuthor) / 2).toFloat(), y + 60f, Colors.SCREEN_TEXT)
        }
    }

    private inner class BookPageTitle(private val x: Int, private val y: Int, page: Page) : Element, Drawable, TickingElement {
        private val wrappedTitleLines = textRenderer.wrapLines(page.title.copy().styled { it.withBold(true) }, PAGE_TITLE_WIDTH)

        private val icons: List<ItemStack> = interleave(page.icons.map { it.createStacks() })
        private var icon = 0
        private var iconTicks = 0

        override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
            itemRenderer.renderGuiItemIcon(icons[icon], x, y)

            val titleY = (y + 10 - textRenderer.fontHeight * wrappedTitleLines.size / 2).toFloat()
            for ((i, line) in wrappedTitleLines.withIndex()) {
                textRenderer.draw(matrices, line, (x + PAGE_TITLE_X).toFloat(), titleY + i * textRenderer.fontHeight, Colors.SCREEN_TEXT)
            }
        }

        override fun tick() {
            if (iconTicks++ >= ICON_DURATION) {
                iconTicks = 0
                icon = (icon + 1) % icons.size
            }
        }
    }

    private inner class BookPageBody(private val x: Int, private val y: Int, private val page: Page) : SizedElement, Drawable {
        override val width = PAGE_WIDTH
        private val wrappedBodyLines = textRenderer.wrapLines(page.text, PAGE_WIDTH - PAGE_TEXT_X)
        private val textHeight = wrappedBodyLines.size * textRenderer.fontHeight
        private val imageHeight = if (page.image != null) page.image.size.y + PAGE_IMAGE_GAP else 0
        override val height = max(PAGE_BODY_HEIGHT, textHeight + imageHeight)

        override fun isMouseOver(mouseX: Double, mouseY: Double): Boolean =
            x <= mouseX && mouseX <= x + width && y <= mouseY && mouseY <= y + height

        private fun getTextStyleAt(x: Int, y: Int): Style? {
            if (!isMouseOver(x.toDouble(), y.toDouble())) return null

            // coordinates in widget-space
            val wx = x - (this.x + PAGE_TEXT_X)
            val wy = y - (this.y)
            val lineIndex = wy / textRenderer.fontHeight

            if (lineIndex in wrappedBodyLines.indices) {
                val line = wrappedBodyLines[lineIndex]
                return textRenderer.textHandler.getStyleAt(line, wx)
            }

            return null
        }

        override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
            val textYOffset = if (page.image != null && page.image.placement == Image.Placement.BEFORE_TEXT) {
                imageHeight
            } else {
                0
            }

            for ((i, line) in wrappedBodyLines.withIndex()) {
                textRenderer.draw(matrices, line, (x + PAGE_TEXT_X).toFloat(), (textYOffset + y + i * textRenderer.fontHeight).toFloat(), Colors.SCREEN_TEXT)
            }

            if (page.image != null) {
                renderImage(matrices, page.image, mouseX, mouseY)
            }

            val hoveredStyle = getTextStyleAt(mouseX, mouseY)
            Scissors.suspendScissors {
                renderTextHoverEffect(matrices, hoveredStyle, mouseX, mouseY)
            }
        }

        private fun renderImage(matrices: MatrixStack, image: Image, mouseX: Int, mouseY: Int) {
            val imageX = x + (PAGE_WIDTH - image.size.x) / 2
            val imageY = when (image.placement) {
                Image.Placement.BEFORE_TEXT -> y
                Image.Placement.AFTER_TEXT -> y + textHeight + PAGE_IMAGE_GAP
            }

            RenderSystem.enableBlend()
            RenderSystem.setShader(GameRenderer::getPositionTexShader)
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
            RenderSystem.setShaderTexture(0, image.location)
            drawTexture(matrices, imageX, imageY, 0f, 0f, image.size.x, image.size.y, image.size.x, image.size.y)
            RenderSystem.disableBlend()

            for (hoverArea in image.hoverAreas) {
                if (hoverArea.contains(mouseX - imageX, mouseY - imageY)) {
                    val hX = imageX + hoverArea.position.x
                    val hY = imageY + hoverArea.position.y
                    fill(matrices, hX, hY, hX + hoverArea.size.x, hY + hoverArea.size.y, HOVER_AREA_HIGHLIGHT_COLOR)

                    val wrappedTooltip = client!!.textRenderer.wrapLines(hoverArea.tooltip, PAGE_WIDTH)
                    Scissors.suspendScissors {
                        renderOrderedTooltip(matrices, wrappedTooltip, mouseX, mouseY)
                    }
                    break
                }
            }
        }

        override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
            if (button == 0) {
                val style = getTextStyleAt(mouseX.toInt(), mouseY.toInt())

                if (style != null && handleTextClick(style)) {
                    return true
                }
            }

            return super.mouseClicked(mouseX, mouseY, button)
        }
    }

    private class CloseButton(x: Int, y: Int, pressAction: PressAction) : ButtonWidget(x, y, 8, 8, Text.empty(), pressAction) {
        override fun renderButton(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader)
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
            RenderSystem.setShaderTexture(
                0,
                if (isHovered) {
                    CLOSE_BOOK_ACTIVE_TEXTURE
                } else {
                    CLOSE_BOOK_INACTIVE_TEXTURE
                }
            )

            drawTexture(matrices, x, y, 0f, 0f, 8, 8, 8, 8)
        }
    }
}
