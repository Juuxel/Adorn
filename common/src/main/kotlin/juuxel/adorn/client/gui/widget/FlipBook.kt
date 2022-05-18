package juuxel.adorn.client.gui.widget

import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.Narratable
import net.minecraft.client.gui.Selectable
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder
import net.minecraft.client.util.math.MatrixStack

class FlipBook(private val pageUpdateListener: () -> Unit) : Element, Drawable, Selectable, PageContainer, TickingElement {
    private val pages = ArrayList<Element>()
    override var currentPage = 0
        set(value) {
            field = value
            pageUpdateListener()
        }
    override val pageCount get() = pages.size

    private fun page() = pages[currentPage]

    fun add(page: Element) {
        pages += page
    }

    override fun showPreviousPage() {
        if (hasPreviousPage()) {
            currentPage--
        }
    }

    override fun showNextPage() {
        if (hasNextPage()) {
            currentPage++
        }
    }

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        val page = page()

        if (page is Drawable) {
            page.render(matrices, mouseX, mouseY, delta)
        }
    }

    override fun mouseMoved(mouseX: Double, mouseY: Double) =
        page().mouseMoved(mouseX, mouseY)

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean =
        page().mouseClicked(mouseX, mouseY, button)

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean =
        page().mouseReleased(mouseX, mouseY, button)

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean =
        page().mouseDragged(mouseX, mouseY, button, deltaX, deltaY)

    override fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean =
        page().mouseScrolled(mouseX, mouseY, amount)

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean =
        page().keyPressed(keyCode, scanCode, modifiers)

    override fun keyReleased(keyCode: Int, scanCode: Int, modifiers: Int): Boolean =
        page().keyReleased(keyCode, scanCode, modifiers)

    override fun charTyped(chr: Char, modifiers: Int): Boolean =
        page().charTyped(chr, modifiers)

    override fun changeFocus(lookForwards: Boolean): Boolean =
        page().changeFocus(lookForwards)

    override fun isMouseOver(mouseX: Double, mouseY: Double): Boolean =
        page().isMouseOver(mouseX, mouseY)

    override fun appendNarrations(builder: NarrationMessageBuilder) {
        val page = page()

        if (page is Narratable) {
            page.appendNarrations(builder)
        }
    }

    override fun getType(): Selectable.SelectionType {
        val page = page()

        return if (page is Selectable) {
            page.type
        } else {
            Selectable.SelectionType.NONE
        }
    }

    override fun tick() {
        val page = page()

        if (page is TickingElement) {
            page.tick()
        }
    }
}
