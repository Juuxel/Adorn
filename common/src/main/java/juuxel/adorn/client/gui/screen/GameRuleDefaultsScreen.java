package juuxel.adorn.client.gui.screen;

import juuxel.adorn.config.ConfigManager;
import juuxel.adorn.util.Casing;
import juuxel.adorn.util.PropertyRef;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public final class GameRuleDefaultsScreen extends AbstractConfigScreen {
    private static final int BUTTON_WIDTH = 250;

    public GameRuleDefaultsScreen(Screen parent) {
        super(Text.translatable("gui.adorn.config.game_rule_defaults"), parent);
    }

    @Override
    protected void init() {
        // TODO: make this screen look like the game rule settings screen
        super.init();
        var config = ConfigManager.config();
        addConfigToggle(BUTTON_WIDTH, PropertyRef.ofField(config.gameRuleDefaults, "skipNightOnSofas"));
        addConfigToggle(BUTTON_WIDTH, PropertyRef.ofField(config.gameRuleDefaults, "infiniteKitchenSinks"));
        addConfigToggle(BUTTON_WIDTH, PropertyRef.ofField(config.gameRuleDefaults, "dropLockedTradingStations"));
        addDrawableChild(
            ButtonWidget.builder(ScreenTexts.BACK, button -> close())
                .position(this.width / 2 - 100, this.height - 27)
                .size(200, 20)
                .build()
        );
    }

    @Override
    protected String getOptionTranslationKey(String name) {
        return "gamerule.adorn." + Casing.fromCamelCase().toSnakeCase().convert(name);
    }
}
