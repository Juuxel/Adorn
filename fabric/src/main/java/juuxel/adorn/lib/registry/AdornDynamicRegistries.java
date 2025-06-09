package juuxel.adorn.lib.registry;

import juuxel.adorn.entity.ConeVariant;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;

public final class AdornDynamicRegistries {
    public static void init() {
        DynamicRegistries.registerSynced(AdornRegistryKeys.CONE_VARIANT, ConeVariant.CODEC);
    }
}
