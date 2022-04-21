package juuxel.adorn.datagen

import java.io.Serializable

/**
 * A namespaced ID. Mimics `net.minecraft.util.Identifier`.
 */
data class Id(val namespace: String, val path: String) : Serializable {
    init {
        require(validateIdCharacters(namespace, isPath = false)) {
            "ID namespace '$namespace' contains characters not matching [a-z0-9.-_]"
        }
        require(validateIdCharacters(path, isPath = true)) {
            "ID path '$path' contains characters not matching [a-z0-9.-_/]"
        }
    }

    /**
     * Suffixes the path with a [suffix] without separation.
     */
    fun rawSuffixed(suffix: String): Id = Id(namespace, path + suffix)

    /**
     * Suffixes the path with an underscore-separated [suffix].
     */
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

        private fun validateIdCharacters(component: String, isPath: Boolean): Boolean =
            component.all {
                it in 'a'..'z' || it in '0'..'9' || it == '.' || it == '-' || it == '_' || (isPath && it == '/')
            }
    }
}
