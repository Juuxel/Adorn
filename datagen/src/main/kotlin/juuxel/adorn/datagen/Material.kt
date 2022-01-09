package juuxel.adorn.datagen

interface Material {
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

interface BuildingMaterial : Material {
    val planks: Id // this is really the "base" block of the material so planks or something like cobblestone
    val slab: Id
}

class WoodMaterial(private val baseId: Id, private val fungus: Boolean = false) : BuildingMaterial {
    override val slab = baseId.suffixed("slab")
    override val planks = baseId.suffixed("planks")
    override val prefix = baseId
    override val stick = Material.STICK

    val log =
        if (fungus) baseId.suffixed("stem")
        else baseId.suffixed("log")

    override fun TemplateDsl.appendTemplates() {
        "planks" with planks
        "slab" with slab
        "log" with log
    }
}

class StoneMaterial(private val baseId: Id, private val bricks: Boolean = false) : BuildingMaterial {
    override val slab = baseId.suffixed("slab")
    override val planks =
        if (bricks) baseId.rawSuffixed("s") // brick => bricks
        else baseId
    override val prefix = baseId
    override val stick = Material.STONE_ROD

    override fun TemplateDsl.appendTemplates() {
        "planks" with planks
        "slab" with slab
    }
}

enum class WoolMaterial(private val id: String) : Material {
    WHITE("white"),
    ORANGE("orange"),
    MAGENTA("magenta"),
    LIGHT_BLUE("light_blue"),
    YELLOW("yellow"),
    LIME("lime"),
    PINK("pink"),
    GRAY("gray"),
    LIGHT_GRAY("light_gray"),
    CYAN("cyan"),
    PURPLE("purple"),
    BLUE("blue"),
    BROWN("brown"),
    GREEN("green"),
    RED("red"),
    BLACK("black");

    override val prefix = Id("minecraft", id)
    override val stick = Material.STICK
    val wool = prefix.suffixed("wool")

    override fun TemplateDsl.appendTemplates() {
        "wool" with wool
    }
}
