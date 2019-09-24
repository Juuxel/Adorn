package juuxel.adorn.gui.widget

import io.github.cottonmc.cotton.gui.widget.WWidget
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text

class WCenteredLabel(private val text: Text, private val color: Int) : WWidget() {
    @Environment(EnvType.CLIENT)
    override fun paintBackground(x: Int, y: Int) {
        drawCentered(text.asFormattedString(), x + width / 2, y + height / 2 - 3, color)
    }

    @Environment(EnvType.CLIENT)
    private fun drawCentered(s: String, x: Int, y: Int, color: Int) {
        val render = MinecraftClient.getInstance().fontManager.getTextRenderer(MinecraftClient.DEFAULT_TEXT_RENDERER_ID)
        render!!.draw(s, (x - render.getStringWidth(s) / 2).toFloat(), y.toFloat(), color)
    }

    override fun canResize() = true
}
