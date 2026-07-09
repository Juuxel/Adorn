package juuxel.adorn.platform.forge.compat;

import juuxel.adorn.block.variant.BlockVariantSets;
import juuxel.adorn.config.ConfigManager;
import juuxel.adorn.platform.forge.registrar.ForgeRegistrar;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;

public final class Compat {
    public static void init(IEventBus modBus) {
        ifModLoaded("biomemakeover", () -> BlockVariantSets.add(new BiomeMakeoverCompat()));
        ifModLoaded("biomesoplenty", () -> BlockVariantSets.add(new BiomesOPlentyCompat()));
        ifModLoaded("biomeswevegone", () -> BlockVariantSets.add(new BwgCompat()));
        ifModLoaded("ecologics", () -> BlockVariantSets.add(new EcologicsCompat()));
        ifModLoaded("architects_palette", () -> BlockVariantSets.add(new ArchitectsPaletteCompat()));

        ((ForgeRegistrar<?>) BlockVariantSets.BLOCKS).hook(modBus);
        ((ForgeRegistrar<?>) BlockVariantSets.ITEMS).hook(modBus);
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
        if (isCompatEnabled(mod) && ModList.get().isLoaded(mod)) {
            fn.run();
        }
    }
}
