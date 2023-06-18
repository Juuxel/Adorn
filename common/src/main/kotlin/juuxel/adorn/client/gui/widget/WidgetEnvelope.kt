package juuxel.adorn.client.gui.widget

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.Narratable
import net.minecraft.client.gui.Selectable
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder

/**
 * A wrapper for a widget (obtained by calling [WidgetEnvelope.current()][current]).
 *
 * Used for enhancing the widget with some functionality,
 * such as [pagination][FlipBook], [scissoring][ScissorEnvelope] or
 * [scrolling][ScrollEnvelope].
 */
abstract class WidgetEnvelope : Element, Drawable, Selectable, TickingElement, Draggable {
    protected abstract fun current(): Element

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        val current = current()
        if (current is Drawable) {
            current.render(context, mouseX, mouseY, delta)
        }
    }

    override fun mouseMoved(mouseX: Double, mouseY: Double) =
        current().mouseMoved(mouseX, mouseY)

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean =
        current().mouseClicked(mouseX, mouseY, button)

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean =
        current().mouseReleased(mouseX, mouseY, button)

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean =
        current().mouseDragged(mouseX, mouseY, button, deltaX, deltaY)

    override fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean =
        current().mouseScrolled(mouseX, mouseY, amount)

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean =
        current().keyPressed(keyCode, scanCode, modifiers)

    override fun keyReleased(keyCode: Int, scanCode: Int, modifiers: Int): Boolean =
        current().keyReleased(keyCode, scanCode, modifiers)

    override fun charTyped(chr: Char, modifiers: Int): Boolean =
        current().charTyped(chr, modifiers)

    override fun isFocused(): Boolean =
        current().isFocused

    override fun setFocused(focused: Boolean) {
        current().isFocused = focused
    }

    override fun isMouseOver(mouseX: Double, mouseY: Double): Boolean =
        current().isMouseOver(mouseX, mouseY)

    override fun appendNarrations(builder: NarrationMessageBuilder) {
        val current = current()

        if (current is Narratable) {
            current.appendNarrations(builder)
        }
    }

    override fun getType(): Selectable.SelectionType {
        val current = current()

        return if (current is Selectable) {
            current.type
        } else {
            Selectable.SelectionType.NONE
        }
    }

    override fun tick() {
        val current = current()

        if (current is TickingElement) {
            current.tick()
        }
    }

    override fun stopDragging() {
        val current = current()

        if (current is Draggable) {
            current.stopDragging()
        }
    }
}
