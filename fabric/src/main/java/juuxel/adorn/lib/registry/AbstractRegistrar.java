package juuxel.adorn.lib.registry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractRegistrar<T> implements Registrar<T> {
    protected final List<T> objects = new ArrayList<>();

    @Override
    public Iterator<T> iterator() {
        return objects.iterator();
    }
}
