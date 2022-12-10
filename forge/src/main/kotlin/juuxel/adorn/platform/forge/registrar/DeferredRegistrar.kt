package juuxel.adorn.platform.forge.registrar

import juuxel.adorn.AdornCommon
import juuxel.adorn.lib.Registered
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.RegistryObject
import java.util.function.Supplier

class DeferredRegistrar<T>(registry: RegistryKey<out Registry<T>>) : ForgeRegistrar<T> {
    private val register: DeferredRegister<T> = DeferredRegister.create(registry, AdornCommon.NAMESPACE)
    private val objects: MutableList<RegistryObject<out T>> = ArrayList()

    override fun hook(modBus: IEventBus) {
        register.register(modBus)
    }

    override fun <U : T> register(id: String, provider: () -> U): Registered<U> {
        val registryObject = register.register(id, Supplier(provider))
        objects += registryObject
        return Registered(registryObject::get)
    }

    override fun iterator(): Iterator<T> =
        iterator {
            for (obj in objects) {
                yield(obj.get())
            }
        }
}
