package juuxel.adorn.gui.widget

import com.mojang.blaze3d.systems.RenderSystem
import io.github.cottonmc.cotton.gui.widget.WWidget
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text

class WBigLabel(private val text: Text, private val color: Int, private val scale: Float = DEFAULT_SCALE) : WWidget() {
    @Environment(EnvType.CLIENT)
    private val font = MinecraftClient.getInstance().fontManager.getTextRenderer(MinecraftClient.DEFAULT_TEXT_RENDERER_ID)!!

    @Environment(EnvType.CLIENT)
    override fun paintBackground(x: Int, y: Int) {
        RenderSystem.pushMatrix()
        RenderSystem.translatef((x + width / 2).toFloat(), (y + height / 2 - 3).toFloat(), 0f)
        RenderSystem.scalef(scale, scale, 1.0f)
        val s = text.asFormattedString()
        font.draw(s, (-font.getStringWidth(s) / 2).toFloat(), 0f, color)
        RenderSystem.popMatrix()
    }

    override fun canResize() = true

    companion object {
        const val DEFAULT_SCALE = 1.5f
    }
}
