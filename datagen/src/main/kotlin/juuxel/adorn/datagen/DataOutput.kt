package juuxel.adorn.datagen

fun interface DataOutput {
    fun write(path: String, content: String)
}
