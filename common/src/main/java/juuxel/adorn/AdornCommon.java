package juuxel.adorn;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public final class AdornCommon {
    public static final String NAMESPACE = "adorn";

    public static Identifier id(String path) {
        return Identifier.of(NAMESPACE, path);
    }

    public static <T> RegistryKey<T> key(RegistryKey<? extends Registry<T>> registry, String path) {
        return RegistryKey.of(registry, id(path));
    }
}
