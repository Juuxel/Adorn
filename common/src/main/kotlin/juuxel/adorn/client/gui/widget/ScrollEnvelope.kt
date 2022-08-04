package juuxel.adorn.client.gui.widget

import juuxel.adorn.util.Colors
import juuxel.adorn.util.color
import net.minecraft.client.gui.Element
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.MathHelper

class ScrollEnvelope(x: Int, y: Int, width: Int, height: Int, private val element: SizedElement) : ScissorEnvelope(x, y, width, height) {
    private var offset = 0

    // Scroll bar
    private val trackHeight = height - 2 * SCROLLING_TRACK_MARGIN
    private var draggingThumb = false
    private var dragStart = 0.0

    private fun heightDifference() = element.height - height
    private fun thumbHeight() = (height.toFloat() / element.height.toFloat() * trackHeight.toFloat()).toInt()
    private fun coordToOffsetRatio(): Float = (trackHeight - thumbHeight()).toFloat() / heightDifference().toFloat()
    private fun thumbY(): Int = (offset * coordToOffsetRatio()).toInt()

    private fun isMouseOverThumb(mouseX: Double, mouseY: Double): Boolean {
        if (heightDifference() > 0) {
            val thumbX = x + width - SCROLLING_TRACK_MARGIN - SCROLLING_TRACK_WIDTH
            val thumbStartY = y + SCROLLING_TRACK_MARGIN + thumbY()
            val thumbEndY = thumbStartY + thumbHeight()

            if (thumbX <= mouseX && mouseX <= thumbX + SCROLLING_TRACK_WIDTH && thumbStartY <= mouseY && mouseY <= thumbEndY) {
                return true
            }
        }

        return false
    }

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

        val heightDifference = heightDifference()
        if (heightDifference > 0) {
            if (heightDifference - offset > 0) {
                fillGradient(matrices, x, y + height - GRADIENT_HEIGHT, x + width, y + height, Colors.TRANSPARENT, GRADIENT_COLOR)
            }

            if (offset > 0) {
                fillGradient(matrices, x, y, x + width, y + GRADIENT_HEIGHT, GRADIENT_COLOR, Colors.TRANSPARENT)
            }

            val thumbColor = if (draggingThumb || isMouseOverThumb(mouseX.toDouble(), mouseY.toDouble())) {
                SCROLL_THUMB_COLOR_ACTIVE
            } else {
                SCROLL_THUMB_COLOR_INACTIVE
            }

            val thumbX = x + width - SCROLLING_TRACK_MARGIN - SCROLLING_TRACK_WIDTH
            val thumbY = y + SCROLLING_TRACK_MARGIN + thumbY()
            fill(matrices, thumbX, thumbY, thumbX + SCROLLING_TRACK_WIDTH, thumbY + thumbHeight(), thumbColor)
        }
    }

    override fun mouseMoved(mouseX: Double, mouseY: Double) =
        super.mouseMoved(mouseX, mouseY + offset)

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (isMouseOverThumb(mouseX, mouseY)) {
            draggingThumb = true
            dragStart = mouseY - (y + SCROLLING_TRACK_MARGIN + thumbY())
            return true
        }

        return super.mouseClicked(mouseX, mouseY + offset, button)
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
        if (draggingThumb) {
            val realY = mouseY - dragStart
            val pos = MathHelper.clamp(
                MathHelper.getLerpProgress(
                    realY,
                    (y + SCROLLING_TRACK_MARGIN).toDouble(),
                    (y + SCROLLING_TRACK_MARGIN + trackHeight - thumbHeight()).toDouble()
                ),
                0.0,
                1.0
            )
            offset = (pos * heightDifference()).toInt()
            return true
        }

        return super.mouseDragged(mouseX, mouseY + offset, button, deltaX, deltaY)
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean {
        val heightDifference = heightDifference()

        if (heightDifference > 0) {
            offset = MathHelper.clamp(offset - (amount * SCROLLING_SPEED).toInt(), 0, heightDifference)
        }

        return true
    }

    override fun stopDragging() {
        super.stopDragging()
        draggingThumb = false
    }

    companion object {
        private const val GRADIENT_HEIGHT = 5
        private const val SCROLLING_SPEED = 10
        private val GRADIENT_COLOR = color(0x000000, alpha = 0.2f)
        private const val SCROLLING_TRACK_MARGIN = 2
        private const val SCROLLING_TRACK_WIDTH = 4
        private val SCROLL_THUMB_COLOR_INACTIVE = color(0x000000, alpha = 0.2f)
        private val SCROLL_THUMB_COLOR_ACTIVE = color(0x000000, alpha = 0.8f)
    }
}
