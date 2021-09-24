package juuxel.adorn.platform.forge.client.gui.screen

import com.mojang.blaze3d.systems.RenderSystem
import juuxel.adorn.AdornCommon
import juuxel.adorn.platform.forge.menu.TradingStationMenu
import juuxel.adorn.util.Colors
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText

class TradingStationScreen(
    menu: TradingStationMenu,
    playerInventory: PlayerInventory,
    title: Text
) : AdornMenuScreen<TradingStationMenu>(menu, playerInventory, title) {
    init {
        backgroundHeight = 186
        playerInventoryTitleY = backgroundHeight - 94 // copied from MenuScreen.<init>
    }

    override fun drawBackground(matrices: MatrixStack, delta: Float, mouseX: Int, mouseY: Int) {
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE)
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight)
    }

    override fun drawForeground(matrices: MatrixStack, mouseX: Int, mouseY: Int) {
        super.drawForeground(matrices, mouseX, mouseY)
        textRenderer.draw(matrices, SELLING_LABEL, 26f + 9f - textRenderer.getWidth(SELLING_LABEL) / 2, 25f, Colors.SCREEN_TEXT)
        textRenderer.draw(matrices, PRICE_LABEL, 26f + 9f - textRenderer.getWidth(PRICE_LABEL) / 2, 61f, Colors.SCREEN_TEXT)
    }

    companion object {
        private val BACKGROUND_TEXTURE = AdornCommon.id("textures/gui/trading_station.png")
        private val SELLING_LABEL: Text = TranslatableText("block.adorn.trading_station.selling")
        private val PRICE_LABEL: Text = TranslatableText("block.adorn.trading_station.price")
    }
}
