package juuxel.adorn.client.gui

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.math.max
import kotlin.math.min

/**
 * A global GL scissor stack that is applied when [pushed][push] and [popped][pop].
 */
object Scissors {
    private val stack: ArrayDeque<Frame> = ArrayDeque()

    /**
     * Pushes a new scissor frame at ([x], [y]) with dimensions ([width], [height])
     * and refreshes the scissor state.
     */
    fun push(x: Int, y: Int, width: Int, height: Int) =
        push(Frame(x, y, x + width, y + height))

    /**
     * Pushes a scissor frame and refreshes the scissor state.
     */
    fun push(frame: Frame) {
        stack.addLast(frame)
        apply()
    }

    /**
     * Pops the topmost scissor frame and refreshes the scissor state.
     * If there are no remaining frames, disables scissoring.
     */
    fun pop(): Frame {
        val frame = stack.removeLast()
        apply()
        return frame
    }

    /**
     * Temporarily disables the topmost scissor frame for the [fn].
     */
    @OptIn(ExperimentalContracts::class)
    inline fun suspendScissors(fn: () -> Unit) {
        contract { callsInPlace(fn, InvocationKind.EXACTLY_ONCE) }
        val frame = pop()
        fn()
        push(frame)
    }

    private fun apply() {
        val window = MinecraftClient.getInstance().window

        if (stack.isEmpty()) {
            RenderSystem.disableScissor()
            return
        }

        var x1 = 0
        var y1 = 0
        var x2 = window.scaledWidth
        var y2 = window.scaledHeight

        for (frame in stack) {
            x1 = max(x1, frame.x1)
            y1 = max(y1, frame.y1)
            x2 = min(x2, frame.x2)
            y2 = min(y2, frame.y2)
        }

        val scale = window.scaleFactor
        RenderSystem.enableScissor(
            (x1 * scale).toInt(), (window.framebufferHeight - scale * y2).toInt(),
            ((x2 - x1) * scale).toInt(), ((y2 - y1) * scale).toInt()
        )
    }

    class Frame(val x1: Int, val y1: Int, val x2: Int, val y2: Int)
}
