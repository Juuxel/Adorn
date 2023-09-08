package juuxel.adorn.client.gui.screen

import juuxel.adorn.config.ConfigManager
import juuxel.adorn.fluid.FluidUnit
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.screen.ScreenTexts
import net.minecraft.text.Text

class MainConfigScreen(parent: Screen) : AbstractConfigScreen(Text.translatable("gui.adorn.config.title"), parent) {
    override fun init() {
        super.init()
        val config = ConfigManager.config()
        val x = (width - BUTTON_WIDTH) / 2
        addHeading(Text.translatable("gui.adorn.config.visual"), BUTTON_WIDTH)
        addConfigToggle(BUTTON_WIDTH, config.client::showTradingStationTooltips)
        addConfigToggle(BUTTON_WIDTH, config.client::showItemsInStandardGroups)
        addConfigButton(BUTTON_WIDTH, config.client::displayedFluidUnit, FluidUnit.values().toList())
        addHeading(Text.translatable("gui.adorn.config.other"), BUTTON_WIDTH)
        addDrawableChild(
            ButtonWidget(x, nextChildY, BUTTON_WIDTH, 20, Text.translatable("gui.adorn.config.game_rule_defaults")) {
                client!!.setScreen(GameRuleDefaultsScreen(this))
            }
        )
        addDrawableChild(ButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, ScreenTexts.DONE) { close() })
    }

    companion object {
        private const val BUTTON_WIDTH = 200
    }
}
