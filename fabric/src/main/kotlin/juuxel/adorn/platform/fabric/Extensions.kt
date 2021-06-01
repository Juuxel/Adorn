package juuxel.adorn.platform.fabric

import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback
import net.minecraft.util.registry.Registry

/**
 * Registers a [visitor] for this registry that will be called for each
 * entry currently in the registry, and all future entries.
 */
inline fun <A> Registry<A>.visit(crossinline visitor: (A) -> Unit) {
    this.forEach(visitor)

    RegistryEntryAddedCallback.event(this)
        .register(
            RegistryEntryAddedCallback { _, _, entry ->
                visitor(entry)
            }
        )
}
