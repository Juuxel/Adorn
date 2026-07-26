package juuxel.adorn.client.gui;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.trading.Trade;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderPipelines;
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
    public void extractImage(Font textRenderer, int x, int y, int width, int height, GuiGraphicsExtractor context) {
        context.item(trade.getPrice(), x, y);
        context.itemDecorations(textRenderer, trade.getPrice(), x, y);
        context.item(trade.getSelling(), x + 2 * 16, y);
        context.itemDecorations(textRenderer, trade.getSelling(), x + 2 * 16, y);
        context.blit(RenderPipelines.GUI_TEXTURED, ARROW_TEXTURE, x + 16, y, 0f, 0f, 16, 16, 16, 16);
    }
}
