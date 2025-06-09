package juuxel.adorn.lib.registry;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.entity.ConeVariant;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public final class AdornRegistryKeys {
    public static final RegistryKey<Registry<ConeVariant>> CONE_VARIANT = create("cone_variant");

    private static <T> RegistryKey<Registry<T>> create(String id) {
        return RegistryKey.ofRegistry(AdornCommon.id(id));
    }
}
