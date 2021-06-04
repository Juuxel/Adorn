package juuxel.adorn.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import org.jetbrains.annotations.NotNull;

public final class MenuBridge {
    @ExpectPlatform
    @NotNull
    public static ScreenHandler createDrawerMenu(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        return PlatformCore.expected();
    }

    @ExpectPlatform
    @NotNull
    public static ScreenHandler createKitchenCupboardMenu(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        return PlatformCore.expected();
    }

    @ExpectPlatform
    @NotNull
    public static ScreenHandler createTradingStationMenu(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, boolean owner) {
        return PlatformCore.expected();
    }
}
