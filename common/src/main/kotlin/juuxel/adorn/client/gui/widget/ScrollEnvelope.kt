package juuxel.adorn.client.gui.widget

import juuxel.adorn.util.Colors
import juuxel.adorn.util.color
import net.minecraft.client.gui.Element
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.MathHelper

class ScrollEnvelope(x: Int, y: Int, width: Int, height: Int, private val element: SizedElement) : ScissorEnvelope(x, y, width, height) {
    private var offset = 0

    override fun current(): Element = element

    override fun isMouseOver(mouseX: Double, mouseY: Double): Boolean =
        isWithinScissor(mouseX, mouseY)

    override fun isMouseWithinScissorForInput(x: Double, y: Double) =
        isMouseOver(x, y - offset)

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        matrices.push()
        matrices.translate(0.0, -offset.toDouble(), 0.0)
        super.render(matrices, mouseX, mouseY + offset, delta)
        matrices.pop()

        if (element.height > height) {
            if (element.height - offset > height) {
                fillGradient(matrices, x, y + height - GRADIENT_HEIGHT, x + width, y + height, Colors.TRANSPARENT, GRADIENT_COLOR)
            }

            if (offset > 0) {
                fillGradient(matrices, x, y, x + width, y + GRADIENT_HEIGHT, GRADIENT_COLOR, Colors.TRANSPARENT)
            }
        }
    }

    override fun mouseMoved(mouseX: Double, mouseY: Double) =
        super.mouseMoved(mouseX, mouseY + offset)

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int) =
        super.mouseClicked(mouseX, mouseY + offset, button)

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int) =
        super.mouseReleased(mouseX, mouseY + offset, button)

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double) =
        super.mouseDragged(mouseX, mouseY + offset, button, deltaX, deltaY)

    override fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean {
        val heightDelta = element.height - height

        if (heightDelta > 0) {
            offset = MathHelper.clamp(offset - (amount * SCROLLING_SPEED).toInt(), 0, heightDelta)
        }

        return true
    }

    companion object {
        private const val GRADIENT_HEIGHT = 5
        private const val SCROLLING_SPEED = 6
        private val GRADIENT_COLOR = color(0x000000, alpha = 0.2f)
    }
}
