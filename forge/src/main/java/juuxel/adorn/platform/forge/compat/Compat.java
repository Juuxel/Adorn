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
        ifModLoaded("byg", () -> BlockVariantSets.add(new BygCompat()));
        ifModLoaded("ecologics", () -> BlockVariantSets.add(new EcologicsCompat()));
        ifModLoaded("architects_palette", () -> BlockVariantSets.add(new ArchitectsPaletteCompat()));

        ((ForgeRegistrar<?>) BlockVariantSets.BLOCKS).hook(modBus);
        ((ForgeRegistrar<?>) BlockVariantSets.ITEMS).hook(modBus);
    }

    private static void ifModLoaded(String mod, Runnable fn) {
        if (ConfigManager.isCompatEnabled(mod) && ModList.get().isLoaded(mod)) {
            fn.run();
        }
    }
}
