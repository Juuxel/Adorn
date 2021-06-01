package juuxel.adorn.lib

import java.util.function.Supplier
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun interface Registered<out T> : () -> T, ReadOnlyProperty<Any?, T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T = invoke()

    companion object {
        @JvmStatic
        fun <T> of(supplier: Supplier<T>): Registered<T> = Registered { supplier.get() }
    }
}
