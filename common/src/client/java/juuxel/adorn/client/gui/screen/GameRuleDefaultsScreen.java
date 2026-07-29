package juuxel.adorn.client.gui.screen;

import juuxel.adorn.client.gui.widget.Panel;
import juuxel.adorn.config.ConfigManager;
import juuxel.adorn.util.Casing;
import juuxel.adorn.util.PropertyRef;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public final class GameRuleDefaultsScreen extends AbstractConfigScreen {
    private static final Layout LAYOUT = new Layout.Tabular(250, 44);

    public GameRuleDefaultsScreen(Screen parent) {
        super(Component.translatable("gui.adorn.config.game_rule_defaults"), parent, LAYOUT);
    }

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(
            Button.builder(CommonComponents.GUI_BACK, button -> onClose())
                .pos(width / 2 - 100, height - BACK_BUTTON_Y_FROM_BOTTOM)
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
        return "gamerule.adorn." + Casing.fromCamelCase().toSnakeCase().convert(name);
    }
}
