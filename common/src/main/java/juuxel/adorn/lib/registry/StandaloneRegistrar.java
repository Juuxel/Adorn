package juuxel.adorn.lib.registry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

public final class StandaloneRegistrar<T> implements Registrar<T> {
    private final List<T> objects = new ArrayList<>();

    @Override
    public <U extends T> Registered<U> register(String id, Supplier<? extends U> provider) {
        U value = provider.get();
        objects.add(value);
        return () -> value;
    }

    @Override
    public Iterator<T> iterator() {
        return objects.iterator();
    }
}
