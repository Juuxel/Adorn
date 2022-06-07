package juuxel.adorn.platform.forge.registrar

import juuxel.adorn.AdornCommon
import juuxel.adorn.lib.Registered
import net.minecraft.item.Item
import net.minecraft.util.registry.Registry
import net.minecraft.util.registry.RegistryKey
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.eventbus.api.SubscribeEvent

/**
 * A registrar for registries that don't have a Forge registry.
 *
 * We need a registrar for them too because **Mojang** has started freezing
 * registries as well - just registering e.g. recipe types anywhere doesn't
 * work anymore. Forge's `DeferredRegister` on the other hand doesn't work
 * for types without a Forge registries.
 *
 * "welcome to hell" - max, 31 March 2022
 */
class WelcomeToHell<T>(private val registry: Registry<T>) : ForgeRegistrar<T> {
    private val entries: MutableMap<RegistryKey<T>, () -> T> = HashMap()

    override fun hook(modBus: IEventBus) {
        modBus.register(this)
    }

    // This is ~extremely dirty~ filthy - I hijack the item registry event for any T.
    @SubscribeEvent
    fun register(event: RegistryEvent.Register<Item>) {
        val iter = entries.entries.iterator()

        while (iter.hasNext()) {
            val (key, provider) = iter.next()
            Registry.register(registry, key, provider.invoke())
            iter.remove()
        }
    }

    override fun <U : T> register(id: String, provider: () -> U): Registered<U> {
        val key = RegistryKey.of(registry.key, AdornCommon.id(id))
        entries[key] = provider
        @Suppress("UNCHECKED_CAST")
        return Registered { registry[key] as U }
    }
}
