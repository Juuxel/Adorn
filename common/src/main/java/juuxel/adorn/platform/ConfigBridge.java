package juuxel.adorn.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import juuxel.adorn.config.CommonConfig;
import org.jetbrains.annotations.NotNull;

public final class ConfigBridge {
    @ExpectPlatform
    @NotNull
    public static CommonConfig get() {
        return PlatformCore.expected();
    }
}
