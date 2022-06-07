package juuxel.adorn.platform.forge.registrar

import juuxel.adorn.AdornCommon
import juuxel.adorn.lib.Registered
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.IForgeRegistry
import net.minecraftforge.registries.IForgeRegistryEntry
import java.util.function.Supplier

class ForgeRegistryRegistrar<T : IForgeRegistryEntry<T>>(registry: IForgeRegistry<T>) : ForgeRegistrar<T> {
    private val register: DeferredRegister<T> = DeferredRegister.create(registry, AdornCommon.NAMESPACE)

    override fun hook(modBus: IEventBus) {
        register.register(modBus)
    }

    override fun <U : T> register(id: String, provider: () -> U): Registered<U> {
        val registryObject = register.register(id, Supplier(provider))
        return Registered(registryObject::get)
    }
}
