package juuxel.adorn.client.gui.screen

import juuxel.adorn.config.ConfigManager
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.screen.ScreenTexts
import net.minecraft.text.Text

class GameRuleDefaultsScreen(parent: Screen) : AbstractConfigScreen(Text.translatable("gui.adorn.config.game_rule_defaults"), parent) {
    override fun init() {
        super.init()
        val config = ConfigManager.config()
        val x = (width - BUTTON_WIDTH) / 2
        addDrawableChild(createConfigToggle(x, 40, BUTTON_WIDTH, config.gameRuleDefaults::skipNightOnSofas))
        addDrawableChild(createConfigToggle(x, 40 + BUTTON_SPACING, BUTTON_WIDTH, config.gameRuleDefaults::infiniteKitchenSinks))
        addDrawableChild(createConfigToggle(x, 40 + 2 * BUTTON_SPACING, BUTTON_WIDTH, config.gameRuleDefaults::dropLockedTradingStations))
        addDrawableChild(ButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, ScreenTexts.BACK) { close() })
    }

    override fun getOptionTranslationKey(name: String): String =
        "gamerule.adorn:$name"

    companion object {
        private const val BUTTON_WIDTH = 250
    }
}
