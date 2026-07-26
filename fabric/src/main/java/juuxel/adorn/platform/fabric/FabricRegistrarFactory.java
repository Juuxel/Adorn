package juuxel.adorn.platform.fabric;

import juuxel.adorn.lib.registry.KeyedRegistrar;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import juuxel.adorn.lib.registry.TrackedDataHandlerRegistrar;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class FabricRegistrarFactory implements RegistrarFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> KeyedRegistrar<T> create(ResourceKey<Registry<T>> registryKey) {
        var registry = ((Registry<Registry<T>>) BuiltInRegistries.REGISTRY).getValue(registryKey);
        if (registry == null) throw new IllegalArgumentException("No registry found for key " + registryKey);
        return new RegistrarImpl<>(registry);
    }

    @Override
    public Registrar<EntityDataSerializer<?>> createForTrackedDataHandlers() {
        return new TrackedDataHandlerRegistrar();
    }
}
