package juuxel.adorn.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ItemBridge {
    @ExpectPlatform
    @NotNull
    public static ItemGroup createAdornItemGroup() {
        return PlatformCore.expected();
    }

    @ExpectPlatform
    @Nullable
    public static Item createGuideBook() {
        return PlatformCore.expected();
    }

    @ExpectPlatform
    @Nullable
    public static Item createTradersManual() {
        return PlatformCore.expected();
    }
}
