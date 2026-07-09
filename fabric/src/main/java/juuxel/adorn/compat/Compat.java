package juuxel.adorn.compat;

import juuxel.adorn.block.variant.BlockVariantSets;
import juuxel.adorn.config.ConfigManager;
import net.fabricmc.loader.api.FabricLoader;

public final class Compat {
    public static void init() {
        ifModLoaded("biomeswevegone", () -> BlockVariantSets.add(new BwgCompat()));
        ifModLoaded("terrestria", () -> BlockVariantSets.add(new TerrestriaCompat()));
        ifModLoaded("towelette", () -> ToweletteCompat.init());
        ifModLoaded("traverse", () -> BlockVariantSets.add(new TraverseCompat()));
        ifModLoaded("woods_and_mires", () -> BlockVariantSets.add(new WamCompat()));
        ifModLoaded("biomemakeover", () -> BlockVariantSets.add(new BiomeMakeoverCompat()));
        ifModLoaded("cinderscapes", () -> BlockVariantSets.add(new CinderscapesCompat()));
        ifModLoaded("promenade", () -> BlockVariantSets.add(new PromenadeCompat()));
        ifModLoaded("techreborn", () -> BlockVariantSets.add(new TechRebornCompat()));
        ifModLoaded("blockus", () -> BlockVariantSets.add(new BlockusCompat()));
        ifModLoaded("biomesoplenty", () -> BlockVariantSets.add(new BiomesOPlentyCompat()));
    }

    private static void ifModLoaded(String mod, Runnable fn) {
        if (ConfigManager.isCompatEnabled(mod) && FabricLoader.getInstance().isModLoaded(mod)) {
            fn.run();
        }
    }
}
