package juuxel.adorn.platform.forge.registrar

import juuxel.adorn.AdornCommon
import juuxel.adorn.lib.Registered
import net.minecraft.util.registry.Registry
import net.minecraft.util.registry.RegistryKey
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import java.util.function.Supplier

class DeferredRegistrar<T>(registry: RegistryKey<out Registry<T>>) : ForgeRegistrar<T> {
    private val register: DeferredRegister<T> = DeferredRegister.create(registry, AdornCommon.NAMESPACE)

    override fun hook(modBus: IEventBus) {
        register.register(modBus)
    }

    override fun <U : T> register(id: String, provider: () -> U): Registered<U> {
        val registryObject = register.register(id, Supplier(provider))
        return Registered(registryObject::get)
    }
}
