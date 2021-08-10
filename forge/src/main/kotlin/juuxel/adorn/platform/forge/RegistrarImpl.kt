package juuxel.adorn.platform.forge

import juuxel.adorn.AdornCommon
import juuxel.adorn.lib.Registered
import juuxel.adorn.platform.Registrar
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.IForgeRegistry
import net.minecraftforge.registries.IForgeRegistryEntry
import java.util.function.Supplier

class RegistrarImpl<T : IForgeRegistryEntry<T>>(registry: IForgeRegistry<T>) : Registrar<T> {
    val register: DeferredRegister<T> = DeferredRegister.create(registry, AdornCommon.NAMESPACE)

    override fun <U : T> register(id: String, provider: () -> U): Registered<U> {
        val registryObject = register.register(id, Supplier(provider))
        return Registered(registryObject::get)
    }
}
