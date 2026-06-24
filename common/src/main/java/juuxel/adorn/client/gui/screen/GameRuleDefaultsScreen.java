package juuxel.adorn.client.gui.screen;

import juuxel.adorn.config.ConfigManager;
import juuxel.adorn.util.Casing;
import juuxel.adorn.util.PropertyRef;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public final class GameRuleDefaultsScreen extends AbstractConfigScreen {
    private static final Layout LAYOUT = new Layout.Tabular(250, 44);

    public GameRuleDefaultsScreen(Screen parent) {
        super(Text.translatable("gui.adorn.config.game_rule_defaults"), parent, LAYOUT);
    }

    @Override
    protected void init() {
        super.init();
        var config = ConfigManager.config();
        addConfigToggle(PropertyRef.ofField(config.gameRuleDefaults, "skipNightOnSofas"));
        addConfigToggle(PropertyRef.ofField(config.gameRuleDefaults, "infiniteKitchenSinks"));
        addConfigToggle(PropertyRef.ofField(config.gameRuleDefaults, "dropLockedTradingStations"));
        addDrawableChild(
            ButtonWidget.builder(ScreenTexts.BACK, button -> close())
                .position(width / 2 - 100, height - BACK_BUTTON_Y_FROM_BOTTOM)
                .size(200, 20)
                .build()
        );
    }

    @Override
    protected String getOptionTranslationKey(String name) {
        return "gamerule.adorn." + Casing.fromCamelCase().toSnakeCase().convert(name);
    }
}
