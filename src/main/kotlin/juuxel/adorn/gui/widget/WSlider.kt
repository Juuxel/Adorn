package juuxel.adorn.gui.widget

import io.github.cottonmc.cotton.gui.client.ScreenDrawing
import io.github.cottonmc.cotton.gui.widget.WWidget
import juuxel.adorn.Adorn
import juuxel.adorn.util.Colors
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.util.math.MathHelper
import kotlin.math.max
import kotlin.math.roundToInt

class WSlider(private val min: Int, private val max: Int) : WWidget() {
    var value = min
        private set(value) {
            field = value
            valueChangeListener(value)
        }
    private var valueToCoordRatio: Float = 0f
    private var coordToValueRatio: Float = 0f
    private var valueChangeListener: (Int) -> Unit = {}
    private var mouseReleaseListener: () -> Unit = {}
    private var dragging = false

    constructor(max: Int) : this(0, max)

    override fun canResize() = true

    override fun onMouseDrag(x: Int, y: Int, button: Int) {
        val trackLeft = getTrackLeft(0)
        if (dragging || (x >= trackLeft && x < trackLeft + TRACK_WIDTH)) {
            dragging = true
            value = MathHelper.clamp(max - (valueToCoordRatio * (y - TRACK_PADDING - THUMB_SIZE / 2)).roundToInt() + min, min, max)
        }
    }

    override fun onClick(x: Int, y: Int, button: Int) {
        onMouseDrag(x, y, button)
        dragging = false
        mouseReleaseListener()
    }

    override fun onMouseUp(x: Int, y: Int, button: Int): WWidget {
        dragging = false
        mouseReleaseListener()
        return this
    }

    override fun setSize(x: Int, y: Int) {
        super.setSize(x, y)
        valueToCoordRatio = (max - min + 1).toFloat() / (y - TRACK_PADDING * 2 - THUMB_SIZE + 1).toFloat()
        coordToValueRatio = 1 / valueToCoordRatio
    }

    fun setValueChangeListener(listener: (value: Int) -> Unit) = apply {
        valueChangeListener = listener
    }

    fun setMouseReleaseListener(listener: () -> Unit) = apply {
        mouseReleaseListener = listener
    }

    fun setValue(value: Int) = apply {
        this.value = MathHelper.clamp(value, min, max)
    }

    @Environment(EnvType.CLIENT)
    override fun paintBackground(x: Int, y: Int) {
        super.paintBackground(x, y)
        val px = 1 / 16f
        val trackX = getTrackLeft(x)
        val trackY = getTrackTop(y)
        val trackWidth = getTrackWidth()
        val trackHeight = getTrackHeight()
        val thumbX = getThumbLeft(x)
        val thumbY = getThumbTop(y)

        ScreenDrawing.rect(TEXTURE, trackX, trackY, trackWidth, 1, 0*px, 8*px, 6*px, 9*px, Colors.WHITE)
        ScreenDrawing.rect(TEXTURE, trackX, trackY + 1, trackWidth, trackHeight - 2, 0*px, 9*px, 6*px, 10*px, Colors.WHITE)
        ScreenDrawing.rect(TEXTURE, trackX, trackY + trackHeight - 1, trackWidth, 1, 0*px, 10*px, 6*px, 11*px, Colors.WHITE)
        ScreenDrawing.rect(TEXTURE, thumbX, thumbY, THUMB_SIZE, THUMB_SIZE, 0*px, 0*px, 8*px, 8*px, Colors.WHITE)
    }

    private fun getThumbLeft(x: Int): Int = x + width / 2 - THUMB_SIZE / 2
    private fun getThumbTop(y: Int): Int = y + height - (coordToValueRatio * (value - min)).roundToInt() - THUMB_SIZE * 3 / 2 + 1

    private fun getTrackLeft(x: Int): Int = x + width / 2 - TRACK_WIDTH / 2
    private fun getTrackTop(y: Int): Int = y + TRACK_PADDING

    private fun getTrackWidth() = TRACK_WIDTH
    private fun getTrackHeight() = height - TRACK_PADDING * 2

    companion object {
        private const val TRACK_PADDING = 3
        private const val TRACK_WIDTH = 6
        private const val THUMB_SIZE = 8

        private val TEXTURE = Adorn.id("textures/gui/slider.png")
    }
}
