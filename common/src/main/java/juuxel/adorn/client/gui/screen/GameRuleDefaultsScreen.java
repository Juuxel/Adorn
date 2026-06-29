package juuxel.adorn.client.gui.screen;

import juuxel.adorn.client.gui.widget.Panel;
import juuxel.adorn.config.ConfigManager;
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
        addDrawableChild(
            ButtonWidget.builder(ScreenTexts.BACK, button -> close())
                .position(width / 2 - 100, height - headerFooterOptions.footerWidgetYFromBottom(BUTTON_HEIGHT))
                .size(200, 20)
                .build()
        );
    }

    @Override
    protected void initConfigWidgets(Panel panel) {
        var config = ConfigManager.config();
        addConfigToggle(PropertyRef.ofField(config.gameRuleDefaults, "skipNightOnSofas"));
        addConfigToggle(PropertyRef.ofField(config.gameRuleDefaults, "infiniteKitchenSinks"));
        addConfigToggle(PropertyRef.ofField(config.gameRuleDefaults, "dropLockedTradingStations"));
    }

    @Override
    protected String getOptionTranslationKey(String name) {
        return "gamerule.adorn:" + name;
    }
}
