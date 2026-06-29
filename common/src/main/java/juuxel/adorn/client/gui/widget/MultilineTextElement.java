package juuxel.adorn.client.gui.widget;

import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;

public final class MultilineTextElement implements Drawable, Element, SizedElement {
    private final MultilineText multilineText;
    private final int x;
    private final int y;
    private final int lineHeight;
    private final int color;
    private boolean focused;

    public MultilineTextElement(MultilineText multilineText, int x, int y, int lineHeight, int color) {
        this.multilineText = multilineText;
        this.x = x;
        this.y = y;
        this.lineHeight = lineHeight;
        this.color = color;
    }

    @Override
    public int getWidth() {
        return multilineText.getMaxWidth();
    }

    @Override
    public int getHeight() {
        return multilineText.count() * lineHeight;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        multilineText.draw(context, x, y, lineHeight, color);
    }

    @Override
    public boolean isFocused() {
        return focused;
    }

    @Override
    public void setFocused(boolean focused) {
        this.focused = focused;
    }
}
