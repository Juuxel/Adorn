package juuxel.adorn.client.gui.screen

import juuxel.adorn.client.gui.widget.ConfigScreenHeading
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
        var nextY = 40
        addDrawable(ConfigScreenHeading(Text.translatable("gui.adorn.config.visual"), x, nextY, BUTTON_WIDTH))
        nextY += ConfigScreenHeading.HEIGHT
        addDrawableChild(createConfigToggle(x, nextY, BUTTON_WIDTH, config.client::showTradingStationTooltips))
        nextY += BUTTON_SPACING
        addDrawableChild(createConfigToggle(x, nextY, BUTTON_WIDTH, config.client::showItemsInStandardGroups))
        nextY += BUTTON_SPACING
        addDrawableChild(createConfigButton(x, nextY, BUTTON_WIDTH, config.client::displayedFluidUnit, FluidUnit.values().toList()))
        nextY += BUTTON_SPACING
        addDrawable(ConfigScreenHeading(Text.translatable("gui.adorn.config.other"), x, nextY, BUTTON_WIDTH))
        nextY += ConfigScreenHeading.HEIGHT
        addDrawableChild(
            ButtonWidget(x, nextY, BUTTON_WIDTH, 20, Text.translatable("gui.adorn.config.game_rule_defaults")) {
                client!!.setScreen(GameRuleDefaultsScreen(this))
            }
        )
        addDrawableChild(ButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, ScreenTexts.DONE) { close() })
    }

    companion object {
        private const val HEADING_HEIGHT = 12
        private const val BUTTON_WIDTH = 200
    }
}
