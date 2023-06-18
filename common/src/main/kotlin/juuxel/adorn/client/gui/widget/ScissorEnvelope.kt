package juuxel.adorn.client.gui.widget

import juuxel.adorn.client.gui.Scissors
import net.minecraft.client.gui.DrawContext

abstract class ScissorEnvelope(
    protected val x: Int, protected val y: Int,
    protected val width: Int, protected val height: Int
) : WidgetEnvelope() {
    protected fun isWithinScissor(mouseX: Double, mouseY: Double) =
        x <= mouseX && mouseX <= x + width && y <= mouseY && mouseY <= y + height

    override fun isMouseOver(mouseX: Double, mouseY: Double): Boolean =
        super.isMouseOver(mouseX, mouseY) && isWithinScissor(mouseX, mouseY)

    protected open fun isMouseWithinScissorForInput(x: Double, y: Double): Boolean =
        isWithinScissor(x, y)

    override fun mouseMoved(mouseX: Double, mouseY: Double) {
        if (!isMouseWithinScissorForInput(mouseX, mouseY)) return
        super.mouseMoved(mouseX, mouseY)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (!isMouseWithinScissorForInput(mouseX, mouseY)) return false
        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (!isMouseWithinScissorForInput(mouseX, mouseY)) return false
        return super.mouseReleased(mouseX, mouseY, button)
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
        if (!isMouseWithinScissorForInput(mouseX, mouseY)) return false
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean {
        if (!isMouseWithinScissorForInput(mouseX, mouseY)) return false
        return super.mouseScrolled(mouseX, mouseY, amount)
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        Scissors.push(x, y, width, height)
        super.render(context, mouseX, mouseY, delta)
        Scissors.pop()
    }
}
