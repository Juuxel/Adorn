package juuxel.adorn.lib.registry;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Registrar<T> extends Iterable<T> {
    /**
     * Registers an object with the id. The object is created using the provider.
     */
    <U extends T> Registered.WithKey<T, U> register(String id, Supplier<? extends U> provider);

    @SuppressWarnings("unchecked")
    static <K, U> RegisteredMap<K, U> registerBy(Collection<? extends K> keys, Function<K, Registered<? extends U>> factory) {
        List<? extends K> keyList;
        if (keys instanceof List<? extends K> l) {
            keyList = l;
        } else {
            keyList = List.copyOf(keys);
        }

        RegisteredMap.MapFactory<K> mapFactory;
        if (keyList.getFirst() instanceof Enum<?> e) {
            mapFactory = RegisteredMap.MapFactory.enumKeys(e.getClass());
        } else {
            mapFactory = RegisteredMap.MapFactory.linkedHashKeys();
        }

        RegisteredMap.Builder<K, U> builder = RegisteredMap.builder(mapFactory);
        for (K key : keyList) {
            builder.put(key, factory.apply(key));
        }

        return builder.build();
    }

    static <K, U> RegisteredMap<K, U> registerBy(K[] keys, Function<K, Registered<? extends U>> factory) {
        return registerBy(Arrays.asList(keys), factory);
    }
}
