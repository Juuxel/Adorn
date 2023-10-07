package juuxel.adorn.client.gui.widget

import juuxel.adorn.util.Colors
import juuxel.adorn.util.animation.AnimatedProperty
import juuxel.adorn.util.animation.AnimatedPropertyWrapper
import juuxel.adorn.util.animation.AnimationEngine
import juuxel.adorn.util.animation.Interpolator
import juuxel.adorn.util.color
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.Element
import net.minecraft.util.math.MathHelper
import org.lwjgl.glfw.GLFW

class ScrollEnvelope(
    x: Int, y: Int, width: Int, height: Int,
    private val element: SizedElement,
    animationEngine: AnimationEngine
) : ScissorEnvelope(x, y, width, height) {
    private var offset = 0.0
        set(value) {
            field = MathHelper.clamp(value, 0.0, heightDifference().toDouble())
        }
    private var animatedOffset by AnimatedPropertyWrapper(
        animationEngine, duration = 50, Interpolator.DOUBLE,
        getter = this::offset, setter = { offset = it }
    )

    // Scroll bar
    private val trackHeight = height - 2 * SCROLLING_TRACK_MARGIN
    private var draggingThumb = false
    private var dragStart = 0.0
    private var thumbHovered = false
    private var thumbColor: Int by AnimatedProperty(
        initial = SCROLL_THUMB_COLOR_INACTIVE,
        animationEngine, duration = 20, Interpolator.COLOR
    )

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

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        val matrices = context.matrices
        matrices.push()
        matrices.translate(0.0, -offset, 0.0)
        super.render(context, mouseX, (mouseY + offset).toInt(), delta)
        matrices.pop()

        val heightDifference = heightDifference()
        if (heightDifference > 0) {
            if (heightDifference - offset >= SHADOW_THRESHOLD) {
                context.fillGradient(x, y + height - GRADIENT_HEIGHT, x + width, y + height, Colors.TRANSPARENT, GRADIENT_COLOR)
            }

            if (offset >= SHADOW_THRESHOLD) {
                context.fillGradient(x, y, x + width, y + GRADIENT_HEIGHT, GRADIENT_COLOR, Colors.TRANSPARENT)
            }

            val hovered = draggingThumb || isMouseOverThumb(mouseX.toDouble(), mouseY.toDouble())
            if (thumbHovered != hovered) {
                thumbHovered = hovered
                thumbColor = if (hovered) {
                    SCROLL_THUMB_COLOR_ACTIVE
                } else {
                    SCROLL_THUMB_COLOR_INACTIVE
                }
            }

            val thumbX = x + width - SCROLLING_TRACK_MARGIN - SCROLLING_TRACK_WIDTH
            val thumbY = y + SCROLLING_TRACK_MARGIN + thumbY()
            context.fill(thumbX, thumbY, thumbX + SCROLLING_TRACK_WIDTH, thumbY + thumbHeight(), thumbColor)
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
            offset = pos * heightDifference()
            return true
        }

        return super.mouseDragged(mouseX, mouseY + offset, button, deltaX, deltaY)
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean {
        val heightDifference = heightDifference()

        if (heightDifference > 0) {
            animatedOffset = MathHelper.clamp(offset - (verticalAmount * SCROLLING_SPEED), 0.0, heightDifference.toDouble())
        }

        return true
    }

    override fun stopDragging() {
        super.stopDragging()
        draggingThumb = false
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        val scrollAmount = when (keyCode) {
            GLFW.GLFW_KEY_UP -> -SCROLLING_SPEED
            GLFW.GLFW_KEY_DOWN -> SCROLLING_SPEED
            GLFW.GLFW_KEY_PAGE_UP -> -height
            GLFW.GLFW_KEY_PAGE_DOWN -> height
            else -> 0
        }

        if (scrollAmount != 0) {
            animatedOffset += scrollAmount
            return true
        }

        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    companion object {
        private const val SHADOW_THRESHOLD = 1.0
        private const val GRADIENT_HEIGHT = 5
        private const val SCROLLING_SPEED = 20
        private val GRADIENT_COLOR = color(0x000000, alpha = 0.2f)
        private const val SCROLLING_TRACK_MARGIN = 2
        private const val SCROLLING_TRACK_WIDTH = 4
        private val SCROLL_THUMB_COLOR_INACTIVE = color(0x000000, alpha = 0.2f)
        private val SCROLL_THUMB_COLOR_ACTIVE = color(0x000000, alpha = 0.6f)
    }
}
