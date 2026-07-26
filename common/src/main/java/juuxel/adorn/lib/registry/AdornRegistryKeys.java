package juuxel.adorn.lib.registry;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.entity.ConeVariant;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class AdornRegistryKeys {
    public static final ResourceKey<Registry<ConeVariant>> CONE_VARIANT = create("cone_variant");

    private static <T> ResourceKey<Registry<T>> create(String id) {
        return ResourceKey.createRegistryKey(AdornCommon.id(id));
    }
}
