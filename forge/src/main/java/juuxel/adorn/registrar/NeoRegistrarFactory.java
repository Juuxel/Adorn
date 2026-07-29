package juuxel.adorn.registrar;

import juuxel.adorn.lib.registry.BlockRegistrar;
import juuxel.adorn.lib.registry.ItemRegistrar;
import juuxel.adorn.lib.registry.KeyedRegistrar;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import net.minecraft.core.Registry;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class NeoRegistrarFactory implements RegistrarFactory {
    @Override
    public <T> KeyedRegistrar<T> create(ResourceKey<Registry<T>> registryKey) {
        return new DeferredRegistrar<>(registryKey);
    }

    @Override
    public BlockRegistrar createBlocks() {
        return new DeferredRegistrar.BlockImpl();
    }

    @Override
    public ItemRegistrar createItems() {
        return new DeferredRegistrar.ItemImpl();
    }

    @Override
    public Registrar<EntityDataSerializer<?>> createForTrackedDataHandlers() {
        return create(NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS);
    }
}
