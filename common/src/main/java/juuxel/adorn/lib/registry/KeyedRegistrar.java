package juuxel.adorn.lib.registry;

import net.minecraft.resources.ResourceKey;

import java.util.function.Function;
import java.util.function.Supplier;

public interface KeyedRegistrar<T> extends Registrar<T> {
    @Override
    <U extends T> Registered.WithKey<T, U> register(String id, Supplier<? extends U> provider);

    /**
     * Registers an object with the id. The object is created using the provider.
     */
    <U extends T> Registered.WithKey<T, U> register(String id, Function<? super ResourceKey<T>, ? extends U> provider);
}
