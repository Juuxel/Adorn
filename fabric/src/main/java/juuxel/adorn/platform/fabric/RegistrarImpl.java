package juuxel.adorn.platform.fabric;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.lib.registry.AbstractRegistrar;
import juuxel.adorn.lib.registry.KeyedRegistrar;
import juuxel.adorn.lib.registry.Registered;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Holder;

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
    public <U extends T> Registered.WithKey<T, U> register(String id, Function<? super ResourceKey<T>, ? extends U> provider) {
        var key = createKey(id);
        U value = provider.apply(key);
        return register(key, value);
    }

    private <U extends T> Registered.WithKey<T, U> register(ResourceKey<T> key, U value) {
        Holder.Reference<T> entry = Registry.registerForHolder(registry, key, value);
        objects.add(value);
        return new Registered.WithKey<>() {
            @Override
            public U get() {
                return value;
            }

            @Override
            public ResourceKey<T> key() {
                return key;
            }

            @Override
            public Holder<T> entry() {
                return entry;
            }
        };
    }

    private ResourceKey<T> createKey(String id) {
        return ResourceKey.create(registry.key(), AdornCommon.id(id));
    }
}
