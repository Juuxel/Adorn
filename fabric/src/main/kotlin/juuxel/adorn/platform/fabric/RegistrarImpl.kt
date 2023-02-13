@file:JvmName("RegistrarKtImpl")

package juuxel.adorn.platform.fabric

import juuxel.adorn.AdornCommon.id
import juuxel.adorn.lib.registry.Registered
import juuxel.adorn.lib.registry.Registrar
import net.minecraft.registry.Registry

class RegistrarImpl<T>(private val registry: Registry<T>) : Registrar<T> {
    private val objects: MutableList<T> = ArrayList()

    override fun <U : T> register(id: String, provider: () -> U): Registered<U> {
        val registered = Registry.register(registry, id(id), provider.invoke())
        objects += registered
        return Registered { registered }
    }

    override fun iterator(): Iterator<T> = objects.iterator()
}
