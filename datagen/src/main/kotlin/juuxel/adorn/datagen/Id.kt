package juuxel.adorn.datagen

import java.io.Serializable

data class Id(val namespace: String, val path: String) : Serializable {
    fun rawSuffixed(suffix: String): Id = Id(namespace, "$path$suffix")
    fun suffixed(suffix: String): Id = rawSuffixed("_$suffix")

    override fun toString() = "$namespace:$path"

    companion object {
        fun parse(id: String): Id {
            val (ns, path) = id.split(":")
            return Id(ns, path)
        }
    }
}
