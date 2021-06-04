package juuxel.adorn.lib

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun interface Registered<out T> : () -> T, ReadOnlyProperty<Any?, T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T = invoke()
}
