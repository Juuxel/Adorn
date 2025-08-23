package juuxel.adorn.client.gui.widget;

import juuxel.adorn.util.Colors;
import juuxel.adorn.util.animation.AnimatedProperty;
import juuxel.adorn.util.animation.AnimatedPropertyWrapper;
import juuxel.adorn.util.animation.AnimationEngine;
import juuxel.adorn.util.animation.Interpolator;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

public final class ScrollEnvelope extends ScissorEnvelope {
    private static final double SHADOW_THRESHOLD = 1.0;
    private static final int GRADIENT_HEIGHT = 5;
    private static final int SCROLLING_SPEED = 20;
    private static final int GRADIENT_COLOR = Colors.color(0x000000, 0.2f);
    private static final int SCROLLING_TRACK_MARGIN = 2;
    private static final int SCROLLING_TRACK_WIDTH = 4;
    private static final int SCROLL_THUMB_COLOR_INACTIVE = Colors.color(0x000000, 0.2f);
    private static final int SCROLL_THUMB_COLOR_ACTIVE = Colors.color(0x000000, 0.6f);

    private final SizedElement element;
    private double offset = 0.0;
    private final AnimatedPropertyWrapper<Double> animatedOffset;

    // Scroll bar
    private final int trackHeight;
    private boolean draggingThumb = false;
    private double dragStart = 0.0;
    private boolean thumbHovered = false;
    private final AnimatedProperty<Integer> thumbColor;

    public ScrollEnvelope(int x, int y, int width, int height, SizedElement element, AnimationEngine animationEngine) {
        super(x, y, width, height);
        this.element = element;
        this.animatedOffset = new AnimatedPropertyWrapper<>(
            animationEngine, 50, Interpolator.DOUBLE,
            () -> offset, this::setOffset
        );
        this.trackHeight = height - 2 * SCROLLING_TRACK_MARGIN;
        this.thumbColor = new AnimatedProperty<>(
            SCROLL_THUMB_COLOR_INACTIVE,
            animationEngine, 20, Interpolator.COLOR
        );
    }

    private void setOffset(double offset) {
        this.offset = MathHelper.clamp(offset, 0.0, heightDifference());
    }

    private int heightDifference() {
        return element.getHeight() - height;
    }

    private int thumbHeight() {
        return (int) ((float) height / (float) element.getHeight() * trackHeight);
    }

    private float coordToOffsetRatio() {
        return (float) (trackHeight - thumbHeight()) / (float) heightDifference();
    }

    private int thumbY() {
        return (int) (offset * coordToOffsetRatio());
    }

    private boolean isMouseOverThumb(double mouseX, double mouseY) {
        if (heightDifference() > 0) {
            int thumbX = x + width - SCROLLING_TRACK_MARGIN - SCROLLING_TRACK_WIDTH;
            int thumbStartY = y + SCROLLING_TRACK_MARGIN + thumbY();
            int thumbEndY = thumbStartY + thumbHeight();

            if (thumbX <= mouseX && mouseX <= thumbX + SCROLLING_TRACK_WIDTH && thumbStartY <= mouseY && mouseY <= thumbEndY) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected Element current() {
        return element;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return isWithinScissor(mouseX, mouseY);
    }

    @Override
    protected boolean isMouseWithinScissorForInput(double x, double y) {
        return isMouseOver(x, y - offset);
    }

    @Override
    protected void renderContent(DrawContext context, int mouseX, int mouseY, float delta) {
        var matrices = context.getMatrices();
        matrices.pushMatrix();
        matrices.translate(0f, (float) -offset);
        super.renderContent(context, mouseX, (int) (mouseY + offset), delta);
        matrices.popMatrix();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        var heightDifference = heightDifference();
        if (heightDifference > 0) {
            if (heightDifference - offset >= SHADOW_THRESHOLD) {
                context.fillGradient(x, y + height - GRADIENT_HEIGHT, x + width, y + height, Colors.TRANSPARENT, GRADIENT_COLOR);
            }

            if (offset >= SHADOW_THRESHOLD) {
                context.fillGradient(x, y, x + width, y + GRADIENT_HEIGHT, GRADIENT_COLOR, Colors.TRANSPARENT);
            }

            var hovered = draggingThumb || isMouseOverThumb(mouseX, mouseY);
            if (thumbHovered != hovered) {
                thumbHovered = hovered;
                thumbColor.set(hovered ? SCROLL_THUMB_COLOR_ACTIVE : SCROLL_THUMB_COLOR_INACTIVE);
            }

            var thumbX = x + width - SCROLLING_TRACK_MARGIN - SCROLLING_TRACK_WIDTH;
            var thumbY = y + SCROLLING_TRACK_MARGIN + thumbY();
            context.fill(thumbX, thumbY, thumbX + SCROLLING_TRACK_WIDTH, thumbY + thumbHeight(), thumbColor.get());
        }
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY + offset);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOverThumb(mouseX, mouseY)) {
            draggingThumb = true;
            dragStart = mouseY - (y + SCROLLING_TRACK_MARGIN + thumbY());
            return true;
        }

        return super.mouseClicked(mouseX, mouseY + offset, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (draggingThumb) {
            var realY = mouseY - dragStart;
            var pos = MathHelper.clamp(
                MathHelper.getLerpProgress(
                    realY,
                    y + SCROLLING_TRACK_MARGIN,
                    y + SCROLLING_TRACK_MARGIN + trackHeight - thumbHeight()
                ),
                0.0,
                1.0
            );
            setOffset(pos * heightDifference());
            return true;
        }

        return super.mouseDragged(mouseX, mouseY + offset, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        var heightDifference = heightDifference();

        if (heightDifference > 0) {
            animatedOffset.set(MathHelper.clamp(offset - (verticalAmount * SCROLLING_SPEED), 0.0, heightDifference));
        }

        return true;
    }

    @Override
    public void stopDragging() {
        super.stopDragging();
        draggingThumb = false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        var scrollAmount = switch (keyCode) {
            case GLFW.GLFW_KEY_UP -> -SCROLLING_SPEED;
            case GLFW.GLFW_KEY_DOWN -> SCROLLING_SPEED;
            case GLFW.GLFW_KEY_PAGE_UP -> -height;
            case GLFW.GLFW_KEY_PAGE_DOWN -> height;
            default -> 0;
        };

        if (scrollAmount != 0) {
            animatedOffset.set(animatedOffset.get() + scrollAmount);
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
