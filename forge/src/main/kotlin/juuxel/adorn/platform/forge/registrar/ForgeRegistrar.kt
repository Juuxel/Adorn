package juuxel.adorn.platform.forge.registrar

import juuxel.adorn.platform.Registrar
import net.minecraftforge.eventbus.api.IEventBus

/**
 * A registrar that can be registered (or "hooked")
 * to an event bus.
 */
interface ForgeRegistrar<T> : Registrar<T> {
    fun hook(modBus: IEventBus)
}
