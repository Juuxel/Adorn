package juuxel.adorn.lib

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun interface Registered<out T> : ReadOnlyProperty<Any?, T> {
    fun get(): T

    override fun getValue(thisRef: Any?, property: KProperty<*>): T = get()
}
