package juuxel.adorn.client.gui.widget;

import juuxel.adorn.util.Colors;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.tooltip.TooltipState;
import net.minecraft.text.Text;

public record ConfigScreenLabel(Text text, TooltipState tooltipState, int x, int y, int width) implements Drawable {
    public static final int HEIGHT = 20;

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        var client = MinecraftClient.getInstance();
        var textRenderer = client.textRenderer;
        var matrices = context.getMatrices();
        matrices.push();
        matrices.translate(x, y, 0);
        var textY = (HEIGHT - textRenderer.fontHeight) / 2;
        context.drawText(textRenderer, text, 0, textY, Colors.WHITE, false);
        matrices.pop();

        if (x <= mouseX && mouseX <= x + width && y <= mouseY && mouseY <= y + HEIGHT) {
            tooltipState.render(true, false, new ScreenRect(x, y, width, HEIGHT));
        }
    }
}
