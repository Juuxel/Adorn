package juuxel.adorn.client.gui.widget;

import juuxel.adorn.client.gui.Scissors;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;

public abstract class ScissorEnvelope extends WidgetEnvelope {
    protected final int x;
    protected final int y;
    protected final int width;
    protected final int height;

    protected ScissorEnvelope(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected boolean isWithinScissor(double mouseX, double mouseY) {
        return x <= mouseX && mouseX <= x + width && y <= mouseY && mouseY <= y + height;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return super.isMouseOver(mouseX, mouseY) && isWithinScissor(mouseX, mouseY);
    }

    protected boolean isMouseWithinScissorForInput(double x, double y) {
        return isWithinScissor(x, y);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        if (!isMouseWithinScissorForInput(mouseX, mouseY)) return;
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        if (!isMouseWithinScissorForInput(click.x(), click.y())) return false;
        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent click) {
        if (!isMouseWithinScissorForInput(click.x(), click.y())) return false;
        return super.mouseReleased(click);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent click, double offsetX, double offsetY) {
        if (!isMouseWithinScissorForInput(click.x(), click.y())) return false;
        return super.mouseDragged(click, offsetX, offsetY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (!isMouseWithinScissorForInput(mouseX, mouseY)) return false;
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        Scissors.push(context, x, y, width, height);
        extractContent(context, mouseX, mouseY, delta);
        Scissors.pop(context);
    }

    protected void extractContent(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        super.extractRenderState(context, mouseX, mouseY, delta);
    }
}
