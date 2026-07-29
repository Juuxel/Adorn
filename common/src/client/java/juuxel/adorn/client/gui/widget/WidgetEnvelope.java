package juuxel.adorn.client.gui.widget;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.narration.NarrationSupplier;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;

/**
 * A wrapper for a widget (obtained by calling {@link #current()}).
 *
 * <p>Used for enhancing the widget with some functionality,
 * such as {@linkplain FlipBook pagination}, {@linkplain ScissorEnvelope scissoring} or
 * {@linkplain ScrollEnvelope scrolling}.
 */
public abstract class WidgetEnvelope implements GuiEventListener, Renderable, NarratableEntry, TickingElement, Draggable {
    protected abstract GuiEventListener current();

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        if (current() instanceof Renderable drawable) {
            drawable.extractRenderState(context, mouseX, mouseY, delta);
        }
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        current().mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        return current().mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent click) {
        return current().mouseReleased(click);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent click, double offsetX, double offsetY) {
        return current().mouseDragged(click, offsetX, offsetY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return current().mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(KeyEvent input) {
        return current().keyPressed(input);
    }

    @Override
    public boolean keyReleased(KeyEvent input) {
        return current().keyReleased(input);
    }

    @Override
    public boolean charTyped(CharacterEvent input) {
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
    public void updateNarration(NarrationElementOutput builder) {
        if (current() instanceof NarrationSupplier narratable) {
            narratable.updateNarration(builder);
        }
    }

    @Override
    public NarrationPriority narrationPriority() {
        return current() instanceof NarratableEntry selectable ? selectable.narrationPriority() : NarrationPriority.NONE;
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
