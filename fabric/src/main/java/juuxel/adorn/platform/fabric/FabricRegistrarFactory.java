package juuxel.adorn.platform.fabric;

import juuxel.adorn.lib.registry.KeyedRegistrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public final class FabricRegistrarFactory implements RegistrarFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> KeyedRegistrar<T> create(RegistryKey<Registry<T>> registryKey) {
        var registry = ((Registry<Registry<T>>) Registries.REGISTRIES).get(registryKey);
        if (registry == null) throw new IllegalArgumentException("No registry found for key " + registryKey);
        return new RegistrarImpl<>(registry);
    }
}
