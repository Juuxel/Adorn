package juuxel.adorn.lib.registry

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun interface Registered<out T> : ReadOnlyProperty<Any?, T> {
    fun get(): T

    override fun getValue(thisRef: Any?, property: KProperty<*>): T = get()
}
