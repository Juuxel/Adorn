package juuxel.adorn.datagen

interface Material {
    val slab: Id
    val planks: Id // this is really the "base" block of the material so planks or something like cobblestone
    val prefix: Id
    val stick: Id

    fun isModded(): Boolean =
        prefix.namespace != "minecraft"

    fun TemplateDsl.appendTemplates()

    companion object {
        val STICK = Id("c", "wooden_rods")
        val STONE_ROD = Id("c", "stone_rods")
    }
}

class WoodMaterial(private val baseId: Id, private val fungus: Boolean = false) : Material {
    override val slab = baseId.suffixed("slab")
    override val planks = baseId.suffixed("planks")
    override val prefix = baseId
    override val stick = Material.STICK

    val log =
        if (fungus) baseId.suffixed("stem")
        else baseId.suffixed("log")

    override fun TemplateDsl.appendTemplates() {
        "log" with log
    }
}

class StoneMaterial(private val baseId: Id, private val bricks: Boolean = false) : Material {
    override val slab = baseId.suffixed("slab")
    override val planks =
        if (bricks) baseId.rawSuffixed("s") // brick => bricks
        else baseId
    override val prefix = baseId
    override val stick = Material.STONE_ROD

    override fun TemplateDsl.appendTemplates() {
    }
}
