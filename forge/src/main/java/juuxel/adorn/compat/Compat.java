package juuxel.adorn.compat;

import juuxel.adorn.block.variant.BlockVariantSets;
import juuxel.adorn.config.ConfigManager;
import juuxel.adorn.registrar.NeoRegistrar;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;

public final class Compat {
    public static void init(IEventBus modBus) {
        ifModLoaded("biomemakeover", () -> BlockVariantSets.add(new BiomeMakeoverCompat()));
        ifModLoaded("biomesoplenty", () -> BlockVariantSets.add(new BiomesOPlentyCompat()));
        ifModLoaded("biomeswevegone", () -> BlockVariantSets.add(new BwgCompat()));
        ifModLoaded("ecologics", () -> BlockVariantSets.add(new EcologicsCompat()));
        ifModLoaded("architects_palette", () -> BlockVariantSets.add(new ArchitectsPaletteCompat()));

        ((NeoRegistrar<?>) BlockVariantSets.BLOCKS).hook(modBus);
        ((NeoRegistrar<?>) BlockVariantSets.ITEMS).hook(modBus);
    }

    private static void ifModLoaded(String mod, Runnable fn) {
        if (ConfigManager.isCompatEnabled(mod) && ModList.get().isLoaded(mod)) {
            fn.run();
        }
    }
}
