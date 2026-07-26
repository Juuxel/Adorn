package juuxel.adorn.client.gui.widget;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;

import java.util.ArrayList;
import java.util.List;

public class Panel extends AbstractContainerEventHandler implements Renderable, TickingElement, Draggable {
    private final List<GuiEventListener> children = new ArrayList<>();
    private final List<Renderable> drawables = new ArrayList<>();

    @Override
    public List<? extends GuiEventListener> children() {
        return children;
    }

    public void add(GuiEventListener element) {
        children.add(element);

        if (element instanceof Renderable drawable) {
            drawables.add(drawable);
        }
    }

    public void addStandaloneDrawable(Renderable drawable) {
        if (drawable instanceof GuiEventListener) {
            throw new IllegalArgumentException("Elements cannot be added with addStandaloneDrawable");
        }

        drawables.add(drawable);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        for (var child : drawables) {
            child.extractRenderState(context, mouseX, mouseY, delta);
        }
    }

    @Override
    public void tick() {
        for (var child : children) {
            if (child instanceof TickingElement ticking) ticking.tick();
        }
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        for (var child : children) {
            if (child.isMouseOver(mouseX, mouseY)) return true;
        }

        return false;
    }

    @Override
    public void stopDragging() {
        for (var child : children) {
            if (child instanceof Draggable draggable) draggable.stopDragging();
        }
    }

    public SizedElement asSized(int width, int height) {
        return new AsSized(width, height);
    }

    private final class AsSized extends WidgetEnvelope implements SizedElement {
        private final int width;
        private final int height;

        private AsSized(int width, int height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public int getWidth() {
            return width;
        }

        @Override
        public int getHeight() {
            return height;
        }

        @Override
        protected GuiEventListener current() {
            return Panel.this;
        }
    }
}
