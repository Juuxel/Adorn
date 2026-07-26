package juuxel.adorn.client.gui.screen;

import juuxel.adorn.client.gui.widget.Panel;
import juuxel.adorn.config.ConfigManager;
import juuxel.adorn.fluid.FluidUnit;
import juuxel.adorn.item.group.ItemGroupingOption;
import juuxel.adorn.util.PropertyRef;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.Arrays;

public final class MainConfigScreen extends AbstractConfigScreen {
    private static final int BUTTON_WIDTH = 200;
    private static final Layout LAYOUT = new Layout.BigButtons(BUTTON_WIDTH);

    public MainConfigScreen(Screen parent) {
        super(Component.translatable("gui.adorn.config.title"), parent, LAYOUT);
    }

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(
            Button.builder(CommonComponents.GUI_DONE, widget -> onClose())
                .pos(width / 2 - 100, height - BACK_BUTTON_Y_FROM_BOTTOM)
                .size(200, 20)
                .build()
        );
    }

    @Override
    protected void initConfigWidgets(Panel panel) {
        var config = ConfigManager.config();
        int x = (width - BUTTON_WIDTH) / 2;
        addHeading(Component.translatable("gui.adorn.config.visual"));
        addConfigToggle(PropertyRef.ofField(config.client, "showTradingStationTooltips"));
        addConfigButton(PropertyRef.ofField(config.client, "displayedFluidUnit"), Arrays.asList(FluidUnit.values()));
        addHeading(Component.translatable("gui.adorn.config.creative_inventory"));
        addConfigToggle(PropertyRef.ofField(config.client, "showItemsInStandardGroups"));
        addConfigButton(
            PropertyRef.ofField(config, "groupItems"),
            Arrays.asList(ItemGroupingOption.values()),
            true
        );
        addHeading(Component.translatable("gui.adorn.config.other"));
        addSubscreenButton(Component.translatable("gui.adorn.config.game_rule_defaults"), GameRuleDefaultsScreen::new);
        addSubscreenButton(Component.translatable("gui.adorn.config.toggle_mod_compatibility"), ModCompatConfigScreen::new);
    }
}
