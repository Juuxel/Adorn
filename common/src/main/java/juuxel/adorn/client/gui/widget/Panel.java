package juuxel.adorn.client.gui.widget;

import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;

import java.util.ArrayList;
import java.util.List;

public class Panel extends AbstractParentElement implements Drawable, TickingElement, Draggable {
    private final List<Element> children = new ArrayList<>();
    private final List<Drawable> drawables = new ArrayList<>();

    @Override
    public List<? extends Element> children() {
        return children;
    }

    public void add(Element element) {
        children.add(element);

        if (element instanceof Drawable drawable) {
            drawables.add(drawable);
        }
    }

    public void addStandaloneDrawable(Drawable drawable) {
        if (drawable instanceof Element) {
            throw new IllegalArgumentException("Elements cannot be added with addStandaloneDrawable");
        }

        drawables.add(drawable);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        for (var child : drawables) {
            child.render(context, mouseX, mouseY, delta);
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
        protected Element current() {
            return Panel.this;
        }
    }
}
