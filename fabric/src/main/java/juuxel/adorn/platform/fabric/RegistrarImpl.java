package juuxel.adorn.platform.fabric;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.Registrar;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public final class RegistrarImpl<T> implements Registrar<T> {
    private final Registry<T> registry;
    private final List<T> objects = new ArrayList<>();

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
        Registry.register(registry, key, value);
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
        };
    }

    private RegistryKey<T> createKey(String id) {
        return AdornCommon.key(registry.getKey(), id);
    }

    @Override
    public Iterator<T> iterator() {
        return objects.iterator();
    }
}
