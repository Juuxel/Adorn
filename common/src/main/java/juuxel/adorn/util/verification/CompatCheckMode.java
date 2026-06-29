package juuxel.adorn.util.verification;

import juuxel.adorn.util.Displayable;
import net.minecraft.text.Text;

public enum CompatCheckMode implements Displayable {
    OFF("off"),
    LOG_ONLY("log_only"),
    LOG_AND_GUI("log_and_gui");

    private final Text displayName;

    CompatCheckMode(String id) {
        this.displayName = Text.translatable("gui.adorn.compat_check_mode." + id);
    }

    public boolean enabled() {
        return this != OFF;
    }

    public boolean showGui() {
        return this == LOG_AND_GUI;
    }

    @Override
    public Text getDisplayName() {
        return displayName;
    }
}
