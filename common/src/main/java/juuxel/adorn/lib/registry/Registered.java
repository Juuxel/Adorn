package juuxel.adorn.lib.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Holder;

import java.util.function.Supplier;

@FunctionalInterface
public interface Registered<T> extends Supplier<T> {
    interface WithKey<R, T extends R> extends Registered<T> {
        ResourceKey<R> key();
        Holder<R> entry();
    }
}
