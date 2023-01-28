package juuxel.adorn.util

class ResettableLazy<T>(private val computer: () -> T) {
    private var value: T? = null
    private var hasValue = false

    @Suppress("UNCHECKED_CAST")
    @Synchronized
    fun get(): T {
        if (hasValue) return value as T
        val current = computer()
        value = current
        hasValue = true
        return current
    }

    @Synchronized
    fun reset() {
        value = null
        hasValue = false
    }
}
