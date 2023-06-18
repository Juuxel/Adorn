package juuxel.adorn.client.gui

import juuxel.adorn.AdornCommon
import juuxel.adorn.trading.Trade
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.tooltip.TooltipComponent

class TradeTooltipComponent(private val trade: Trade) : TooltipComponent {
    override fun getWidth(textRenderer: TextRenderer?): Int = 3 * 16
    override fun getHeight(): Int = 18

    override fun drawItems(textRenderer: TextRenderer, x: Int, y: Int, context: DrawContext) {
        context.drawItem(trade.price, x, y)
        context.drawItemInSlot(textRenderer, trade.price, x, y)
        context.drawItem(trade.selling, x + 2 * 16, y)
        context.drawItemInSlot(textRenderer, trade.selling, x + 2 * 16, y)
        context.drawTexture(ARROW_TEXTURE, x + 16, y, 0f, 0f, 16, 16, 16, 16)
    }

    companion object {
        private val ARROW_TEXTURE = AdornCommon.id("textures/gui/tooltip_arrow.png")
    }
}
