package juuxel.adorn.client.gui.screen;

import juuxel.adorn.client.gui.widget.Panel;
import juuxel.adorn.config.ConfigManager;
import juuxel.adorn.fluid.FluidUnit;
import juuxel.adorn.item.group.ItemGroupingOption;
import juuxel.adorn.util.PropertyRef;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import java.util.Arrays;

public final class MainConfigScreen extends AbstractConfigScreen {
    private static final int BUTTON_WIDTH = 200;
    private static final Layout LAYOUT = new Layout.BigButtons(BUTTON_WIDTH);

    public MainConfigScreen(Screen parent) {
        super(Text.translatable("gui.adorn.config.title"), parent, LAYOUT);
    }

    @Override
    protected void init() {
        super.init();
        addDrawableChild(
            ButtonWidget.builder(ScreenTexts.DONE, widget -> close())
                .position(width / 2 - 100, height - BACK_BUTTON_Y_FROM_BOTTOM)
                .size(200, 20)
                .build()
        );
    }

    @Override
    protected void initConfigWidgets(Panel panel) {
        var config = ConfigManager.config();
        int x = (width - BUTTON_WIDTH) / 2;
        addHeading(Text.translatable("gui.adorn.config.visual"));
        addConfigToggle(PropertyRef.ofField(config.client, "showTradingStationTooltips"));
        addConfigButton(PropertyRef.ofField(config.client, "displayedFluidUnit"), Arrays.asList(FluidUnit.values()));
        addHeading(Text.translatable("gui.adorn.config.creative_inventory"));
        addConfigToggle(PropertyRef.ofField(config.client, "showItemsInStandardGroups"));
        addConfigButton(
            PropertyRef.ofField(config, "groupItems"),
            Arrays.asList(ItemGroupingOption.values()),
            true
        );
        addHeading(Text.translatable("gui.adorn.config.mod_compatibility"));
        addConfigToggle(PropertyRef.ofField(config, "checkModCompatIssues"), true);
        addSubscreenButton(Text.translatable("gui.adorn.config.toggle_mod_compatibility"), ModCompatConfigScreen::new);
        addHeading(Text.translatable("gui.adorn.config.other"));
        addSubscreenButton(Text.translatable("gui.adorn.config.game_rule_defaults"), GameRuleDefaultsScreen::new);
    }
}
