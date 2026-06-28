package juuxel.adorn.util;

import java.lang.invoke.MethodHandles;
import java.util.Map;

/**
 * Reference to a mutable property.
 */
public interface PropertyRef<T> {
    String getName();
    T get();
    void set(T value);

    static <T> PropertyRef<T> ofField(Object owner, String fieldName) {
        try {
            var field = owner.getClass().getField(fieldName);
            field.setAccessible(true);

            var name = field.getName();
            var lookup = MethodHandles.lookup();
            var getter = lookup.unreflectGetter(field);
            var setter = lookup.unreflectSetter(field);

            return new PropertyRef<>() {
                @Override
                public String getName() {
                    return name;
                }

                @SuppressWarnings("unchecked")
                @Override
                public T get() {
                    try {
                        return (T) getter.invoke(owner);
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void set(T value) {
                    try {
                        setter.invoke(owner, value);
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                }
            };
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    static <T> PropertyRef<T> ofMapEntry(Map<String, T> map, String key) {
        return new PropertyRef<>() {
            @Override
            public String getName() {
                return key;
            }

            @Override
            public T get() {
                return map.get(key);
            }

            @Override
            public void set(T value) {
                map.put(key, value);
            }
        };
    }
}
