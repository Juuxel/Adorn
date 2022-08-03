package juuxel.adorn.client.gui

import com.mojang.blaze3d.systems.RenderSystem
import juuxel.adorn.AdornCommon
import juuxel.adorn.trading.Trade
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.gui.tooltip.TooltipComponent
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.item.ItemRenderer
import net.minecraft.client.util.math.MatrixStack

class TradeTooltipComponent(private val trade: Trade) : TooltipComponent {
    override fun getWidth(textRenderer: TextRenderer?): Int = 3 * 16
    override fun getHeight(): Int = 18

    override fun drawItems(textRenderer: TextRenderer, x: Int, y: Int, matrices: MatrixStack, itemRenderer: ItemRenderer, z: Int) {
        itemRenderer.renderInGuiWithOverrides(trade.price, x, y, 0)
        itemRenderer.renderGuiItemOverlay(textRenderer, trade.price, x, y)
        itemRenderer.renderInGuiWithOverrides(trade.selling, x + 2 * 16, y, 1)
        itemRenderer.renderGuiItemOverlay(textRenderer, trade.selling, x + 2 * 16, y)

        RenderSystem.setShader(GameRenderer::getPositionTexShader)
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        RenderSystem.setShaderTexture(0, ARROW_TEXTURE)
        DrawableHelper.drawTexture(matrices, x + 16, y, z, 0f, 0f, 16, 16, 16, 16)
    }

    companion object {
        private val ARROW_TEXTURE = AdornCommon.id("textures/gui/tooltip_arrow.png")
    }
}
