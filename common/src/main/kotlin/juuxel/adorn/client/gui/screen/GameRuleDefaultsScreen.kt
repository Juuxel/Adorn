package juuxel.adorn.client.gui.screen

import juuxel.adorn.config.ConfigManager
import juuxel.adorn.util.property
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.screen.ScreenTexts
import net.minecraft.text.Text

class GameRuleDefaultsScreen(parent: Screen) : AbstractConfigScreen(Text.translatable("gui.adorn.config.game_rule_defaults"), parent) {
    override fun init() {
        super.init()
        val config = ConfigManager.config()
        addConfigToggle(BUTTON_WIDTH, config.gameRuleDefaults.property { it::skipNightOnSofas })
        addConfigToggle(BUTTON_WIDTH, config.gameRuleDefaults.property { it::infiniteKitchenSinks })
        addConfigToggle(BUTTON_WIDTH, config.gameRuleDefaults.property { it::dropLockedTradingStations })
        addDrawableChild(
            ButtonWidget.builder(ScreenTexts.BACK) { close() }
                .position(this.width / 2 - 100, this.height - 27)
                .size(200, 20)
                .build()
        )
    }

    override fun getOptionTranslationKey(name: String): String =
        "gamerule.adorn:$name"

    companion object {
        private const val BUTTON_WIDTH = 250
    }
}
