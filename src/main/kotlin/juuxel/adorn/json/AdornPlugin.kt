package juuxel.adorn.json

import io.github.cottonmc.jsonfactory.gens.ContentGenerator
import io.github.cottonmc.jsonfactory.gens.GeneratorInfo
import io.github.cottonmc.jsonfactory.gens.block.SuffixedBlockItemModel
import io.github.cottonmc.jsonfactory.gens.block.SuffixedBlockState
import io.github.cottonmc.jsonfactory.gens.block.SuffixedLootTable
import io.github.cottonmc.jsonfactory.plugin.Plugin

object AdornPlugin : Plugin {
    init {
        // TODO: Remove this from JF, use a system similar to subcategories
        GeneratorInfo.Categories.addCategory(AdornCategory)
    }

    //adorn:red,adorn:black,adorn:green,adorn:brown,adorn:blue,adorn:purple,adorn:cyan,adorn:light_gray,adorn:gray,adorn:pink,adorn:lime,adorn:yellow,adorn:light_blue,adorn:magenta,adorn:orange,adorn:white

    val SOFA = GeneratorInfo(AdornCategory, Subcategories.Sofas)
    val CHAIR = GeneratorInfo(AdornCategory, Subcategories.Chairs)
    val TABLE = GeneratorInfo(AdornCategory, Subcategories.Tables)
    val KITCHEN = GeneratorInfo(AdornCategory, Subcategories.Kitchen)
    override val generators: List<ContentGenerator> = listOf(
        SofaBlockModel,
        SofaBlockState,
        SofaItemModel,
        SuffixedLootTable("Sofa", "sofa", SOFA),

        ChairBlockModel,
        ChairBlockState,
        SuffixedBlockItemModel("Chair", "chair", CHAIR),
        SuffixedLootTable("Chair", "chair", CHAIR),

        TableBlockModel,
        SuffixedBlockState("Table Block State", "table", TABLE),
        SuffixedBlockItemModel("Table", "table", TABLE),
        SuffixedLootTable("Table", "table", TABLE),

        KitchenCounterBlockModel,
        KitchenCounterBlockState,
        SuffixedBlockItemModel("Kitchen Counter", "kitchen_counter", KITCHEN),
        SuffixedLootTable("Kitcher Counter", "kitchen_counter", KITCHEN),

        KitchenCupboardBlockModel,
        KitchenCupboardBlockState,
        KitchenCupboardItemModel,
        SuffixedLootTable("Kitcher Cupboard", "kitchen_cupboard", KITCHEN)
    )

    object AdornCategory : GeneratorInfo.Category {
        override val description = null
        override val displayName = "Adorn"
        // TODO: only used by placeholder textures, figure out a better name for this
        override val path = ""
    }

    enum class Subcategories(override val displayName: String, override val description: String? = null) : GeneratorInfo.Subcategory {
        Sofas("Sofas"),
        Chairs("Chairs"),
        Tables("Tables"),
        Kitchen("Kitchen")
    }

    @JvmStatic
    fun main(args: Array<String>) {
        io.github.cottonmc.jsonfactory.gui.main(
            arrayOf(AdornPlugin::class.qualifiedName!!)
        )
    }
}
