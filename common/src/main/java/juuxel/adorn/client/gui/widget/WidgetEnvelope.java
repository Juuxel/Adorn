package juuxel.adorn.client.gui.widget;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Narratable;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;

/**
 * A wrapper for a widget (obtained by calling {@link #current()}).
 *
 * <p>Used for enhancing the widget with some functionality,
 * such as {@linkplain FlipBook pagination}, {@linkplain ScissorEnvelope scissoring} or
 * {@linkplain ScrollEnvelope scrolling}.
 */
public abstract class WidgetEnvelope implements Element, Drawable, Selectable, TickingElement, Draggable {
    protected abstract Element current();

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (current() instanceof Drawable drawable) {
            drawable.render(context, mouseX, mouseY, delta);
        }
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        current().mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        return current().mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseReleased(Click click) {
        return current().mouseReleased(click);
    }

    @Override
    public boolean mouseDragged(Click click, double offsetX, double offsetY) {
        return current().mouseDragged(click, offsetX, offsetY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return current().mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        return current().keyPressed(input);
    }

    @Override
    public boolean keyReleased(KeyInput input) {
        return current().keyReleased(input);
    }

    @Override
    public boolean charTyped(CharInput input) {
        return current().charTyped(input);
    }

    @Override
    public boolean isFocused() {
        return current().isFocused();
    }

    @Override
    public void setFocused(boolean focused) {
        current().setFocused(focused);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return current().isMouseOver(mouseX, mouseY);
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
        if (current() instanceof Narratable narratable) {
            narratable.appendNarrations(builder);
        }
    }

    @Override
    public SelectionType getType() {
        return current() instanceof Selectable selectable ? selectable.getType() : SelectionType.NONE;
    }

    @Override
    public void tick() {
        if (current() instanceof TickingElement ticking) {
            ticking.tick();
        }
    }

    @Override
    public void stopDragging() {
        if (current() instanceof Draggable draggable) {
            draggable.stopDragging();
        }
    }
}
