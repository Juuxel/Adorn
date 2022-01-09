package juuxel.adorn.datagen

data class Id(val namespace: String, val path: String) {
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
