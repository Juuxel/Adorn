package juuxel.adorn.gui.widget

import io.github.cottonmc.cotton.gui.client.ScreenDrawing
import io.github.cottonmc.cotton.gui.widget.WWidget
import juuxel.adorn.util.Colors
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.ingame.InventoryScreen
import net.minecraft.util.Identifier

class WPlayerPanel : WWidget() {
    init {
        super.setSize(2 * 18, 2 * 18 + 9)
    }

    override fun canResize() = false

    override fun setSize(x: Int, y: Int) {}

    override fun paintBackground(x: Int, y: Int, mouseX: Int, mouseY: Int) {
        val px = 1 / 256f
        ScreenDrawing.rect(TEXTURE, x, y, width, height, 72 * px, 5 * px, 106 * px, 50 * px, Colors.WHITE)
        InventoryScreen.drawEntity(x + width / 2, y + height - 4, 20, -mouseX.toFloat(), -mouseY.toFloat(), MinecraftClient.getInstance().player)
    }

    companion object {
        private val TEXTURE = Identifier("minecraft", "textures/gui/container/creative_inventory/tab_inventory.png")
    }
}
