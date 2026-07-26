package juuxel.adorn.client.gui;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.trading.Trade;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.Identifier;

public record TradeTooltipComponent(Trade trade) implements ClientTooltipComponent {
    private static final Identifier ARROW_TEXTURE = AdornCommon.id("textures/gui/tooltip_arrow.png");

    @Override
    public int getWidth(Font textRenderer) {
        return 3 * 16;
    }

    @Override
    public int getHeight(Font textRenderer) {
        return 18;
    }

    @Override
    public void renderImage(Font textRenderer, int x, int y, int width, int height, GuiGraphics context) {
        context.renderItem(trade.getPrice(), x, y);
        context.renderItemDecorations(textRenderer, trade.getPrice(), x, y);
        context.renderItem(trade.getSelling(), x + 2 * 16, y);
        context.renderItemDecorations(textRenderer, trade.getSelling(), x + 2 * 16, y);
        context.blit(RenderPipelines.GUI_TEXTURED, ARROW_TEXTURE, x + 16, y, 0f, 0f, 16, 16, 16, 16);
    }
}
