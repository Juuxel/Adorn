package juuxel.adorn.platform

import juuxel.adorn.lib.Registered

interface Registrar<T> {
    /**
     * Registers an object with the [id]. The object is created using the [provider].
     */
    fun <U : T> register(id: String, provider: () -> U): Registered<U>

    /**
     * Registers an optional object with the [id]. The [provider] can either create the object,
     * or return null, in which case the return value contains null.
     *
     * Note that this, unlike the other methods, always invokes the provider eagerly even on Forge.
     */
    fun <U : T> registerOptional(id: String, provider: () -> U?): Registered<U?> {
        val value: U = provider.invoke() ?: return Registered { null }
        return register(id) { value }
    }
}
