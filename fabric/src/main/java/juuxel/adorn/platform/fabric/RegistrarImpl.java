package juuxel.adorn.platform.fabric;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.lib.registry.KeyedRegistrar;
import juuxel.adorn.lib.registry.Registered;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public final class RegistrarImpl<T> implements KeyedRegistrar<T> {
    private final Registry<T> registry;
    private final List<T> objects = new ArrayList<>();

    public RegistrarImpl(Registry<T> registry) {
        this.registry = registry;
    }

    @Override
    public <U extends T> Registered.WithKey<T, U> register(String id, Supplier<? extends U> provider) {
        return register(id, key -> provider.get());
    }

    @Override
    public <U extends T> Registered.WithKey<T, U> register(String id, Function<? super RegistryKey<T>, ? extends U> provider) {
        var key = RegistryKey.of(registry.getKey(), AdornCommon.id(id));
        var registered = provider.apply(key);
        var entry = Registry.registerReference(registry, key, registered);
        objects.add(registered);
        return new Registered.WithKey<>() {
            @Override
            public U get() {
                return registered;
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

    @Override
    public Iterator<T> iterator() {
        return objects.iterator();
    }
}
