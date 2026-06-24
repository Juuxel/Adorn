package juuxel.adorn.client.gui.widget;

import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;

import java.util.ArrayList;
import java.util.List;

public class Panel extends AbstractParentElement implements Drawable, TickingElement, Draggable {
    private final List<Element> children = new ArrayList<>();

    @Override
    public List<? extends Element> children() {
        return children;
    }

    public void add(Element element) {
        children.add(element);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        for (var child : children) {
            if (child instanceof Drawable drawable) drawable.render(context, mouseX, mouseY, delta);
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
}
