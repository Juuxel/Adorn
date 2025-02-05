package juuxel.adorn.compat;

import juuxel.adorn.block.variant.BlockVariantSets;
import juuxel.adorn.config.ConfigManager;
import net.fabricmc.loader.api.FabricLoader;

public final class Compat {
    public static void init() {
        BlockVariantSets.loadCompatSets();
        ifModLoaded("towelette", () -> ToweletteCompat.init());
    }

    private static void ifModLoaded(String mod, Runnable fn) {
        if (ConfigManager.isCompatEnabled(mod) && FabricLoader.getInstance().isModLoaded(mod)) {
            fn.run();
        }
    }
}
