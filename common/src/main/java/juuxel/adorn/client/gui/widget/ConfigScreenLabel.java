package juuxel.adorn.client.gui.widget;

import juuxel.adorn.util.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.WidgetTooltipHolder;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.Component;

public record ConfigScreenLabel(Component text, WidgetTooltipHolder tooltipState, int x, int y, int width) implements Renderable {
    public static final int HEIGHT = 20;

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        var client = Minecraft.getInstance();
        var textRenderer = client.font;
        var matrices = context.pose();
        matrices.pushMatrix();
        matrices.translate(x, y);
        var textY = (HEIGHT - textRenderer.lineHeight) / 2;
        context.text(textRenderer, text, 0, textY, Colors.WHITE, false);
        matrices.popMatrix();

        if (x <= mouseX && mouseX <= x + width && y <= mouseY && mouseY <= y + HEIGHT) {
            int realMouseY = ScrollEnvelope.getRealMouseY(mouseY);
            tooltipState.refreshTooltipForNextRenderPass(context, mouseX, realMouseY, true, false, new ScreenRectangle(x, y, width, HEIGHT));
        }
    }
}
