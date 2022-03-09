package juuxel.adorn.client.gui.screen

import juuxel.adorn.platform.PlatformBridges
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.ScreenTexts
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.text.TranslatableText

class MainConfigScreen(private val parent: Screen) : AbstractConfigScreen(TranslatableText("gui.adorn.config.title"), parent) {
    override fun init() {
        val config = PlatformBridges.configManager.config
        val x = (width - BUTTON_WIDTH) / 2
        addDrawableChild(createConfigToggle(x, 40, BUTTON_WIDTH, config.client::showTradingStationTooltips))
        addDrawableChild(createConfigToggle(x, 64, BUTTON_WIDTH, config.client::showItemsInStandardGroups))
        addDrawableChild(
            ButtonWidget(x, 88, BUTTON_WIDTH, 20, TranslatableText("gui.adorn.config.game_rule_defaults")) {
                client!!.setScreen(GameRuleDefaultsScreen(this))
            }
        )
        addDrawableChild(ButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, ScreenTexts.DONE) { close() })
    }

    companion object {
        private const val BUTTON_WIDTH = 200
    }
}
