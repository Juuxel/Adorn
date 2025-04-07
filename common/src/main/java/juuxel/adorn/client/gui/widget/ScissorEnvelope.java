package juuxel.adorn.client.gui.widget;

import juuxel.adorn.client.gui.Scissors;
import net.minecraft.client.gui.DrawContext;

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
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!isMouseWithinScissorForInput(mouseX, mouseY)) return false;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (!isMouseWithinScissorForInput(mouseX, mouseY)) return false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (!isMouseWithinScissorForInput(mouseX, mouseY)) return false;
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (!isMouseWithinScissorForInput(mouseX, mouseY)) return false;
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        Scissors.push(context, x, y, width, height);
        renderContent(context, mouseX, mouseY, delta);
        Scissors.pop(context);
    }

    protected void renderContent(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }
}
