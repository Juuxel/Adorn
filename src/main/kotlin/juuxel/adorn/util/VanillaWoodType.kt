package juuxel.adorn.util

enum class VanillaWoodType(val id: String) {
    Oak("oak"),
    Spruce("spruce"),
    Birch("birch"),
    Jungle("jungle"),
    Acacia("acacia"),
    DarkOak("dark_oak");

    override fun toString() = id
}
