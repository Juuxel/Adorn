package juuxel.adorn.gui.widget

import io.github.cottonmc.cotton.gui.widget.WLabel
import juuxel.adorn.util.client.DrawableHelperInstance
import net.minecraft.client.MinecraftClient
import net.minecraft.text.TextComponent

class CenteredLabelWidget(text: TextComponent, color: Int) : WLabel(text, color) {
    override fun paintBackground(x: Int, y: Int) {
        DrawableHelperInstance.drawCenteredString(
            MinecraftClient.getInstance().fontManager.getTextRenderer(MinecraftClient.DEFAULT_TEXT_RENDERER_ID),
            text.formattedText,
            x + width / 2, y + height / 2, color
        )
    }

    override fun getWidth() = 18
}
