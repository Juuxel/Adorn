package juuxel.adorn.client.gui;

import net.minecraft.client.gui.DrawContext;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * A global GL scissor stack that is applied when pushed and popped.
 */
public final class Scissors {
    private static final Deque<Frame> STACK = new ArrayDeque<>();

    /**
     * Pushes a new scissor frame at {@code (x, y)} with dimensions {@code (width, height)}
     * and refreshes the scissor state.
     */
    public static void push(DrawContext context, int x, int y, int width, int height) {
        push(context, new Frame(x, y, x + width, y + height));
    }

    /**
     * Pushes a scissor frame and refreshes the scissor state.
     */
    public static void push(DrawContext context, Frame frame) {
        STACK.addLast(frame);
        context.enableScissor(frame.x1, frame.y1, frame.x2, frame.y2);
    }

    /**
     * Pops the topmost scissor frame and refreshes the scissor state.
     * If there are no remaining frames, disables scissoring.
     */
    public static Frame pop(DrawContext context) {
        var frame = STACK.removeLast();
        context.disableScissor();
        return frame;
    }

    /**
     * Temporarily disables the topmost scissor frame for executing the runnable.
     */
    public static void suspendScissors(DrawContext context, Runnable fn) {
        var frame = pop(context);
        fn.run();
        push(context, frame);
    }

    public record Frame(int x1, int y1, int x2, int y2) {
    }
}
