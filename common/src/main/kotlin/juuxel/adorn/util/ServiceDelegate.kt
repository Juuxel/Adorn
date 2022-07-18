package juuxel.adorn.util

import java.util.ServiceLoader
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ServiceDelegate<S>(private val type: Class<S>) : ReadOnlyProperty<Any?, S> {
    private val service by lazy {
        ServiceLoader.load(type).findFirst()
            .orElseThrow { RuntimeException("Could not find Adorn platform service ${type.simpleName}") }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): S = service

    companion object {
        inline operator fun <reified S> invoke(): ReadOnlyProperty<Any?, S> = ServiceDelegate(S::class.java)
    }
}
