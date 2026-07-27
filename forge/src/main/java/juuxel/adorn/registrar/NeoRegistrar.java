package juuxel.adorn.registrar;

import juuxel.adorn.lib.registry.Registrar;
import net.neoforged.bus.api.IEventBus;

/**
 * A registrar that can be registered (or "hooked")
 * to an event bus.
 */
public interface NeoRegistrar<T> extends Registrar<T> {
    void hook(IEventBus modBus);
}
