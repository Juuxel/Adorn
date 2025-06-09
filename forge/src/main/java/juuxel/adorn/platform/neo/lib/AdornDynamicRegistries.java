package juuxel.adorn.platform.neo.lib;

import juuxel.adorn.entity.ConeVariant;
import juuxel.adorn.lib.registry.AdornRegistryKeys;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

public final class AdornDynamicRegistries {
    public static void register(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(AdornRegistryKeys.CONE_VARIANT, ConeVariant.CODEC, ConeVariant.CODEC, builder -> {
            builder.defaultKey(ConeVariant.Keys.ORANGE);
        });
    }
}
