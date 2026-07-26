package juuxel.adorn.client.gui.screen;

import juuxel.adorn.client.gui.widget.Panel;
import juuxel.adorn.config.ConfigManager;
import juuxel.adorn.platform.ModBridge;
import juuxel.adorn.util.PropertyRef;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class ModCompatConfigScreen extends AbstractConfigScreen {
    private static final Layout LAYOUT = new Layout.Tabular(250, 44);

    private final List<String> allMods = new ArrayList<>();
    private final List<String> installedMods = new ArrayList<>();

    public ModCompatConfigScreen(Screen parent) {
        super(Component.translatable("gui.adorn.config.toggle_mod_compatibility"), parent, LAYOUT);

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
        addRenderableWidget(
            Button.builder(CommonComponents.GUI_BACK, button -> onClose())
                .pos(width / 2 - 100, height - BACK_BUTTON_Y_FROM_BOTTOM)
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
    protected Component getOptionLabel(String name) {
        if (installedMods.contains(name)) {
            return Component.literal(ModBridge.get().getModName(name));
        }

        return Component.translatable(
            "gui.adorn.config.toggle_mod_compatibility.uninstalled_mod",
            Component.literal(name).withStyle(ChatFormatting.WHITE)
        ).withStyle(ChatFormatting.GRAY);
    }

    @Override
    protected Component getOptionTooltip(String name) {
        return Component.translatable("gui.adorn.config.toggle_mod_compatibility.description", ModBridge.get().getModName(name));
    }
}
