package juuxel.adorn.client.gui.widget;

import juuxel.adorn.util.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.network.chat.Component;

public record ConfigScreenHeading(Component text, int x, int y, int width) implements Renderable {
    public static final int HEIGHT = 18;
    private static final int OUTER_GAP_WIDTH = 5;
    private static final int INNER_GAP_WIDTH = 5;

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        var client = Minecraft.getInstance();
        var textRenderer = client.font;
        var textWidth = textRenderer.width(text);
        var lineWidth = width / 2 - OUTER_GAP_WIDTH - INNER_GAP_WIDTH - textWidth / 2;
        var lineY = HEIGHT / 2 - 2;

        var matrices = context.pose();
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
        var textY = (HEIGHT - textRenderer.lineHeight) / 2;
        context.text(textRenderer, text, (int) textX, textY, Colors.WHITE, false);
        matrices.popMatrix();
    }
}
