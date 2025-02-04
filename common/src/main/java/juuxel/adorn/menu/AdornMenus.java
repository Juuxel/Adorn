package juuxel.adorn.menu;

import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import juuxel.adorn.platform.MenuBridge;
import juuxel.adorn.platform.PlatformBridges;
import net.minecraft.menu.Menu;
import net.minecraft.menu.MenuType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.util.math.BlockPos;

public final class AdornMenus {
    public static final Registrar<MenuType<?>> MENUS = RegistrarFactory.get().create(RegistryKeys.MENU);
    public static final Registered<MenuType<DrawerMenu>> DRAWER =
        MENUS.register("drawer", () -> createType(DrawerMenu::load));
    public static final Registered<MenuType<KitchenCupboardMenu>> KITCHEN_CUPBOARD =
        MENUS.register("kitchen_cupboard", () -> createType(KitchenCupboardMenu::load));
    public static final Registered<MenuType<TradingStationMenu>> TRADING_STATION =
        MENUS.register("trading_station", () -> new MenuType<>(TradingStationMenu::new, FeatureFlags.VANILLA_FEATURES));
    public static final Registered<MenuType<BrewerMenu>> BREWER =
        MENUS.register("brewer", () -> new MenuType<>(BrewerMenu::new, FeatureFlags.VANILLA_FEATURES));

    public static void init() {
    }

    private static <M extends Menu> MenuType<M> createType(MenuBridge.Factory<M, BlockPos> factory) {
        return PlatformBridges.get().getMenus().createType(factory, BlockPos.PACKET_CODEC);
    }
}
