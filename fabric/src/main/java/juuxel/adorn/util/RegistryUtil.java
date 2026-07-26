package juuxel.adorn.util;

import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.core.Registry;

import java.util.function.Consumer;

public final class RegistryUtil {
    /**
     * Registers a visitor for this registry that will be called for each
     * entry currently in the registry, and all future entries.
     */
    public static <A> void visit(Registry<? extends A> registry, Consumer<A> callback) {
        registry.forEach(callback);
        RegistryEntryAddedCallback.event(registry).register((rawId, id, object) -> callback.accept(object));
    }
}
