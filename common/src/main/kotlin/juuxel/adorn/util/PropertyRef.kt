package juuxel.adorn.util

import java.lang.invoke.MethodHandles
import kotlin.reflect.KProperty

/**
 * Reference to a mutable property.
 */
interface PropertyRef<T> {
    val name: String
    fun get(): T
    fun set(value: T)

    companion object {
        /**
         * Creates a reflected property reference for a field of the [owner].
         */
        fun <T> ofField(owner: Any, fieldName: String): PropertyRef<T> {
            val field = owner::class.java.getField(fieldName)
            field.isAccessible = true

            val name = field.name
            val lookup = MethodHandles.lookup()
            val getter = lookup.unreflectGetter(field)
            val setter = lookup.unreflectSetter(field)

            return object : PropertyRef<T> {
                override val name = name

                @Suppress("UNCHECKED_CAST")
                override fun get(): T =
                    getter.invoke(owner) as T

                override fun set(value: T) {
                    setter.invoke(owner, value)
                }
            }
        }
    }
}

/**
 * Creates a reflected property reference for a [JvmField] property of `this` object.
 */
inline fun <O : Any, T> O.ref(finder: (O) -> KProperty<T>): PropertyRef<T> {
    return PropertyRef.ofField(this, finder(this).name)
}
