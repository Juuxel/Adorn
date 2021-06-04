package juuxel.adorn.platform

import juuxel.adorn.lib.Registered

interface Registrar<T> {
    fun <U : T> register(id: String, provider: () -> U): Registered<U>
    fun <U : T> registerOptional(id: String, provider: () -> U?): Registered<U?> {
        val value: U = provider.invoke() ?: return Registered { null }
        return register(id) { value }
    }
}
