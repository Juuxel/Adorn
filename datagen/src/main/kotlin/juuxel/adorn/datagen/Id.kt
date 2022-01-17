package juuxel.adorn.datagen

import java.io.Serializable

data class Id(val namespace: String, val path: String) : Serializable {
    fun rawSuffixed(suffix: String): Id = Id(namespace, "$path$suffix")
    fun suffixed(suffix: String): Id = rawSuffixed("_$suffix")

    override fun toString() = "$namespace:$path"

    companion object {
        fun parse(id: String): Id {
            require(':' in id) { "Id '$id' does not contain colon (:)!" }
            val parts = id.split(":")
            require(parts.size == 2) { "Id '$id' must consist of exactly two parts! Found: ${parts.size}" }
            val (ns, path) = parts
            return Id(ns, path)
        }
    }
}
