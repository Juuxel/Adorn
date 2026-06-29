package juuxel.adorn.client.gui.screen;

import juuxel.adorn.client.gui.widget.Panel;
import juuxel.adorn.config.ConfigManager;
import juuxel.adorn.platform.ModBridge;
import juuxel.adorn.util.PropertyRef;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class ModCompatConfigScreen extends AbstractConfigScreen {
    private static final Layout LAYOUT = new Layout.Tabular(250, 44);

    private final List<String> allMods = new ArrayList<>();
    private final List<String> installedMods = new ArrayList<>();

    public ModCompatConfigScreen(Screen parent) {
        super(Text.translatable("gui.adorn.config.toggle_mod_compatibility"), parent, LAYOUT);

        for (String mod : ConfigManager.config().compat.keySet()) {
            allMods.add(mod);

            if (ModBridge.get().isModLoaded(mod)) {
                installedMods.add(mod);
            }
        }

        allMods.sort(
            Comparator.<String>comparingInt(mod -> installedMods.contains(mod) ? 0 : 1)
                .thenComparing(Comparator.naturalOrder())
        );
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
        var map = ConfigManager.config().compat;

        for (String mod : allMods) {
            addConfigToggle(PropertyRef.ofMapEntry(map, mod), true);
        }
    }

    @Override
    protected Text getOptionLabel(String name) {
        if (installedMods.contains(name)) {
            return Text.literal(ModBridge.get().getModName(name));
        }

        return Text.translatable(
            "gui.adorn.config.toggle_mod_compatibility.uninstalled_mod",
            Text.literal(name).formatted(Formatting.WHITE)
        ).formatted(Formatting.GRAY);
    }

    @Override
    protected Text getOptionTooltip(String name) {
        return Text.translatable("gui.adorn.config.toggle_mod_compatibility.description", ModBridge.get().getModName(name));
    }
}
