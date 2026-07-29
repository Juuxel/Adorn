package juuxel.adorn.client.gui.screen;

import juuxel.adorn.menu.AdornMenus;
import net.minecraft.client.gui.screens.MenuScreens;

public final class AdornMenuScreens {
    public static void register() {
        MenuScreens.register(AdornMenus.DRAWER.get(), DrawerScreen::new);
        MenuScreens.register(AdornMenus.KITCHEN_CUPBOARD.get(), KitchenCupboardScreen::new);
        MenuScreens.register(AdornMenus.TRADING_STATION.get(), TradingStationScreen::new);
        MenuScreens.register(AdornMenus.BREWER.get(), BrewerScreen::new);
    }
}
