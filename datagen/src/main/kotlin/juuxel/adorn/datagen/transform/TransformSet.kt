package juuxel.adorn.datagen.transform

import java.io.Serializable

data class TransformSet(
    val requirements: Map<String, String>,
    val add: Map<String, Any?>,
    val remove: Set<String>,
) : Serializable {
    class Builder : TransformDsl {
        private val requirements: MutableMap<String, String> = HashMap()
        private val add: MutableMap<String, Any?> = LinkedHashMap()
        private val remove: MutableSet<String> = HashSet()

        override fun withProperty(key: String, value: String) {
            requirements[key] = value
        }

        override fun add(key: String, value: Any?) {
            add[key] = value
        }

        override fun remove(key: String) {
            remove += key
        }

        fun build(): TransformSet = TransformSet(requirements, add, remove)
    }
}
