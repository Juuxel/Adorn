package juuxel.adorn.platform.forge.registrar;

import juuxel.adorn.lib.registry.KeyedRegistrar;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class ForgeRegistrarFactory implements RegistrarFactory {
    @Override
    public <T> KeyedRegistrar<T> create(RegistryKey<Registry<T>> registryKey) {
        return new DeferredRegistrar<>(registryKey);
    }

    @Override
    public Registrar<TrackedDataHandler<?>> createForTrackedDataHandlers() {
        return create(NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS);
    }
}
