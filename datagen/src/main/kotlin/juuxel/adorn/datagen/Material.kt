package juuxel.adorn.datagen

interface Material {
    /** A unique ID for this material. */
    val id: Id
    val stick: Id
    /**
     * A unique snowflake string containing all data for this material.
     * Used for up-to-date checks in [GenerateData][juuxel.adorn.datagen.gradle.GenerateData].
     */
    val snowflake: String

    fun isModded(): Boolean =
        id.namespace != "minecraft"

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

class WoodMaterial(override val id: Id, private val fungus: Boolean, val nonFlammable: Boolean) : BuildingMaterial {
    override val slab = id.suffixed("slab")
    override val planks = id.suffixed("planks")
    override val stick = Material.STICK

    val log =
        if (fungus) id.suffixed("stem")
        else id.suffixed("log")

    override val snowflake = "wood/$id/fungus=$fungus"

    override fun TemplateDsl.appendTemplates() {
        "main-texture" with "<planks.namespace>:block/<planks.path>"
        "planks" with planks
        "slab" with slab
        "log" with log
    }
}

class StoneMaterial(override val id: Id, private val bricks: Boolean, val hasSidedTexture: Boolean) : BuildingMaterial {
    override val slab = id.suffixed("slab")
    override val planks =
        if (bricks) id.rawSuffixed("s") // brick => bricks
        else id
    override val stick = Material.STONE_ROD
    override val snowflake = "stone/$id/bricks=$bricks/hasSidedTexture=$hasSidedTexture"

    override fun TemplateDsl.appendTemplates() {
        "main-texture" with "<planks.namespace>:block/<planks.path>"
        "planks" with planks
        "slab" with slab
    }
}

enum class ColorMaterial(private val color: String) : Material {
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

    override val id = Id("minecraft", color)
    override val stick = Material.STICK
    val wool = id.suffixed("wool")
    override val snowflake = "wool/$id"

    override fun TemplateDsl.appendTemplates() {
        "wool" with wool
    }
}
