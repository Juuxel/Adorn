package juuxel.adorn.lens;

import java.util.function.BiConsumer;
import java.util.function.Function;

public interface Lens<S, V> {
    V get(S source);
    void set(S source, V value);

    default <W> Lens<S, W> then(Lens<V, W> next) {
        return new Lens<>() {
            @Override
            public W get(S source) {
                return next.get(Lens.this.get(source));
            }

            @Override
            public void set(S source, W value) {
                next.set(Lens.this.get(source), value);
            }
        };
    }

    final class Simple<S, V> implements Lens<S, V> {
        private final Function<S, V> get;
        private final BiConsumer<S, V> set;

        public Simple(Function<S, V> get, BiConsumer<S, V> set) {
            this.get = get;
            this.set = set;
        }

        @Override
        public V get(S source) {
            return get.apply(source);
        }

        @Override
        public void set(S source, V value) {
            set.accept(source, value);
        }
    }
}
