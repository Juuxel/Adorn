package juuxel.adorn.compat;

import juuxel.adorn.block.variant.BlockVariantSets;
import juuxel.adorn.config.ConfigManager;
import net.fabricmc.loader.api.FabricLoader;

public final class Compat {
    public static void init() {
        BlockVariantSets.loadCompatSets();
        ifModLoaded("towelette", () -> ToweletteCompat.init());
    }

    public static boolean isCompatEnabled(String mod) {
        var compatMap = ConfigManager.config().compat;

        if (!compatMap.containsKey(mod)) {
            compatMap.put(mod, true);
            ConfigManager.get().save();
            return true;
        }

        return compatMap.get(mod);
    }

    private static void ifModLoaded(String mod, Runnable fn) {
        if (isCompatEnabled(mod) && FabricLoader.getInstance().isModLoaded(mod)) {
            fn.run();
        }
    }
}
