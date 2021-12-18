package juuxel.adorn.client.gui.screen

import juuxel.adorn.platform.PlatformBridges
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.ScreenTexts
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.text.TranslatableText

class GameRuleDefaultsScreen(parent: Screen) : AbstractConfigScreen(TranslatableText("gui.adorn.config.game_rule_defaults"), parent) {
    override fun init() {
        val config = PlatformBridges.configManager.config
        val x = (width - BUTTON_WIDTH) / 2
        addDrawableChild(createConfigToggle(x, 40, BUTTON_WIDTH, config.gameRuleDefaults::skipNightOnSofas))
        addDrawableChild(ButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, ScreenTexts.BACK) { onClose() })
    }

    companion object {
        private const val BUTTON_WIDTH = 250
    }
}
