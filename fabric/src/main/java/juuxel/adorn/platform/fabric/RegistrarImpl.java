package juuxel.adorn.platform.fabric;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.lib.registry.AbstractRegistrar;
import juuxel.adorn.lib.registry.KeyedRegistrar;
import juuxel.adorn.lib.registry.Registered;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.function.Function;
import java.util.function.Supplier;

public final class RegistrarImpl<T> extends AbstractRegistrar<T> implements KeyedRegistrar<T> {
    private final Registry<T> registry;

    public RegistrarImpl(Registry<T> registry) {
        this.registry = registry;
    }

    @Override
    public <U extends T> Registered.WithKey<T, U> register(String id, Supplier<? extends U> provider) {
        var key = createKey(id);
        U value = provider.get();
        return register(key, value);
    }

    @Override
    public <U extends T> Registered.WithKey<T, U> register(String id, Function<? super RegistryKey<T>, ? extends U> provider) {
        var key = createKey(id);
        U value = provider.apply(key);
        return register(key, value);
    }

    private <U extends T> Registered.WithKey<T, U> register(RegistryKey<T> key, U value) {
        var entry = Registry.registerReference(registry, key, value);
        objects.add(value);
        return new Registered.WithKey<>() {
            @Override
            public U get() {
                return value;
            }

            @Override
            public RegistryKey<T> key() {
                return key;
            }

            @Override
            public RegistryEntry<T> entry() {
                return entry;
            }
        };
    }

    private RegistryKey<T> createKey(String id) {
        return RegistryKey.of(registry.getKey(), AdornCommon.id(id));
    }
}
