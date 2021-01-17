package juuxel.adorn.client.gui.screen

import juuxel.adorn.Adorn
import juuxel.adorn.menu.TradingStationMenu
import net.minecraft.client.gui.screen.ingame.MenuScreen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text

class TradingStationScreen(
    menu: TradingStationMenu,
    playerInventory: PlayerInventory,
    title: Text
) : MenuScreen<TradingStationMenu>(menu, playerInventory, title) {
    init {
        backgroundHeight = 186
    }

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, tickDelta: Float) {
        renderBackground(matrices)
        super.render(matrices, mouseX, mouseY, tickDelta)
        drawMouseoverTooltip(matrices, mouseX, mouseY)
    }

    override fun drawBackground(matrices: MatrixStack, delta: Float, mouseX: Int, mouseY: Int) {
        client!!.textureManager.bindTexture(BACKGROUND_TEXTURE)
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight)
    }

    companion object {
        private val BACKGROUND_TEXTURE = Adorn.id("textures/gui/trading_station.png")
    }
}
