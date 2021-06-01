package juuxel.adorn.platform.fabric;

import juuxel.adorn.menu.DrawerMenu;
import juuxel.adorn.menu.KitchenCupboardMenu;
import juuxel.adorn.menu.TradingStationMenu;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;

public final class MenuBridgeImpl {
    public static ScreenHandler createDrawerMenu(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        return new DrawerMenu(syncId, playerInventory, context);
    }

    public static ScreenHandler createKitchenCupboardMenu(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        return new KitchenCupboardMenu(syncId, playerInventory, context);
    }

    public static ScreenHandler createTradingStationMenu(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, boolean owner) {
        return new TradingStationMenu(syncId, playerInventory, context, owner);
    }
}
