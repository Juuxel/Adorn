package juuxel.adorn.util

import java.lang.ref.Reference
import java.lang.ref.SoftReference

interface Cache<K, V : Any> {
    operator fun get(key: K): V?
    fun getOrPut(key: K, value: () -> V): V

    private abstract class ReferenceBasedCache<K, V : Any, R : Reference<V>> : Cache<K, V> {
        private val referenceMap: MutableMap<K, R> = HashMap()
        protected abstract fun createReference(value: V): R

        override fun get(key: K): V? =
            referenceMap[key]?.get()

        override fun getOrPut(key: K, value: () -> V): V {
            var currentValue = referenceMap[key]?.get()

            if (currentValue == null) {
                currentValue = value()
                referenceMap[key] = createReference(currentValue)
            }

            return currentValue
        }
    }

    private class SoftCache<K, V : Any> : ReferenceBasedCache<K, V, SoftReference<V>>() {
        override fun createReference(value: V): SoftReference<V> =
            SoftReference(value)
    }

    companion object {
        fun <K, V : Any> soft(): Cache<K, V> = SoftCache()
    }
}
