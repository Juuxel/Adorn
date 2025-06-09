package juuxel.adorn.lib.registry;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.function.Supplier;

@FunctionalInterface
public interface Registered<T> extends Supplier<T> {
    interface WithKey<R, T extends R> extends Registered<T> {
        RegistryKey<R> key();
        RegistryEntry<R> entry();
    }
}
