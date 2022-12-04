@file:JvmName("RegistrarKtImpl")

package juuxel.adorn.platform.fabric

import juuxel.adorn.AdornCommon.id
import juuxel.adorn.lib.Registered
import juuxel.adorn.platform.Registrar
import net.minecraft.util.registry.Registry

class RegistrarImpl<T>(private val registry: Registry<T>) : Registrar<T> {
    override fun <U : T> register(id: String, provider: () -> U): Registered<U> {
        val registered = Registry.register(registry, id(id), provider.invoke())
        return Registered { registered }
    }
}
