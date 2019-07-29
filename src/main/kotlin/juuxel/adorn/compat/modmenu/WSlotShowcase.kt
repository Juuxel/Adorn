package juuxel.adorn.compat.modmenu

import io.github.cottonmc.cotton.gui.client.BackgroundPainter
import io.github.cottonmc.cotton.gui.widget.WWidget
import juuxel.adorn.util.color
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.sound.PositionedSoundInstance
import net.minecraft.sound.SoundEvents
import net.minecraft.text.TranslatableText

@Environment(EnvType.CLIENT)
class WSlotShowcase(
    private vararg val backgrounds: BackgroundPainter,
    private val onClick: (index: Int) -> Unit
) : WWidget() {
    private val backgroundPainter = BackgroundPainter.createColorful(color(0xF7887B))
    private var index = 0

    init {
        require(backgrounds.isNotEmpty()) { "there must be at least one background" }
    }

    fun chooseBackground(painter: BackgroundPainter) {
        index = backgrounds.indexOf(painter)
    }

    override fun onClick(x: Int, y: Int, button: Int) {
        index++
        if (index > backgrounds.lastIndex) index = 0

        MinecraftClient.getInstance().soundManager.play(
            PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f)
        )
        onClick(index)
    }

    override fun paintBackground(x: Int, y: Int) {
        backgroundPainter.paintBackground(x, y, this)
        backgrounds[index].paintBackground(x, y, this)
    }

    override fun addInformation(information: MutableList<String>) {
        information += TranslatableText("gui.adorn.config.slot.click_me").asFormattedString()
    }
}
