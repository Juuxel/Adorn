package juuxel.adorn.client.gui.widget;

import juuxel.adorn.util.Colors;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.text.Text;

public record ConfigScreenHeading(Text text, int x, int y, int width) implements Drawable {
    public static final int HEIGHT = 18;
    private static final int OUTER_GAP_WIDTH = 5;
    private static final int INNER_GAP_WIDTH = 5;

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        var client = MinecraftClient.getInstance();
        var textRenderer = client.textRenderer;
        var textWidth = textRenderer.getWidth(text);
        var lineWidth = width / 2 - OUTER_GAP_WIDTH - INNER_GAP_WIDTH - textWidth / 2;
        var lineY = HEIGHT / 2 - 2;

        var matrices = context.getMatrices();
        matrices.pushMatrix();
        matrices.translate(x, y);

        if (textWidth <= width - 2 * OUTER_GAP_WIDTH - 2 * INNER_GAP_WIDTH) {
            // Left line
            matrices.pushMatrix();
            matrices.translate(OUTER_GAP_WIDTH, 0);
            context.fill(0, lineY, lineWidth, lineY + 1, Colors.WHITE);
            matrices.popMatrix();

            // Right line
            matrices.pushMatrix();
            matrices.translate(width - lineWidth - OUTER_GAP_WIDTH, 0);
            context.fill(0, lineY, lineWidth, lineY + 1, Colors.WHITE);
            matrices.popMatrix();
        }

        // Label
        var textX = (width - textWidth) * 0.5f;
        var textY = (HEIGHT - textRenderer.fontHeight) / 2;
        context.drawText(textRenderer, text, (int) textX, textY, Colors.WHITE, false);
        matrices.popMatrix();
    }
}
