package juuxel.adorn.json

import io.github.cottonmc.jsonfactory.gens.ContentGenerator
import io.github.cottonmc.jsonfactory.gens.GeneratorInfo
import io.github.cottonmc.jsonfactory.gens.block.SuffixedBlockItemModel
import io.github.cottonmc.jsonfactory.gens.block.SuffixedLootTable
import io.github.cottonmc.jsonfactory.plugin.Plugin

object AdornPlugin : Plugin {
    //adorn:red,adorn:black,adorn:green,adorn:brown,adorn:blue,adorn:purple,adorn:cyan,adorn:light_gray,adorn:gray,adorn:pink,adorn:lime,adorn:yellow,adorn:light_blue,adorn:magenta,adorn:orange,adorn:white
    //adorn:oak,adorn:spruce,adorn:birch,adorn:jungle,adorn:acacia,adorn:dark_oak

    val SOFA = GeneratorInfo(AdornCategory, Subcategories.Sofas)
    val CHAIR = GeneratorInfo(AdornCategory, Subcategories.Chairs)
    val TABLE = GeneratorInfo(AdornCategory, Subcategories.Tables)
    val KITCHEN = GeneratorInfo(AdornCategory, Subcategories.Kitchen)
    val DRAWER = GeneratorInfo(AdornCategory, Subcategories.Drawers)
    override val generators: List<ContentGenerator> = listOf(
        SofaBlockModel,
        SofaBlockState,
        SofaItemModel,
        SuffixedLootTable("Sofa", "sofa", SOFA),
        SofaRecipe,

        ChairBlockModel,
        ChairBlockState,
        SuffixedBlockItemModel("Chair", "chair", CHAIR),
        SuffixedLootTable("Chair", "chair", CHAIR),
        ChairRecipe,

        TableBlockModel,
        TableBlockState,
        TableItemModel,
        SuffixedLootTable("Table", "table", TABLE),
        TableRecipe,

        KitchenCounterBlockModel,
        KitchenCounterBlockState,
        SuffixedBlockItemModel("Kitchen Counter", "kitchen_counter", KITCHEN),
        SuffixedLootTable("Kitcher Counter", "kitchen_counter", KITCHEN),
        KitchenCounterRecipe,

        KitchenCupboardBlockModel,
        KitchenCupboardBlockState,
        KitchenCupboardItemModel,
        SuffixedLootTable("Kitcher Cupboard", "kitchen_cupboard", KITCHEN),
        KitchenCupboardRecipe,

        DrawerBlockModel,
        DrawerBlockState,
        SuffixedBlockItemModel("Drawer", "drawer", DRAWER),
        SuffixedLootTable("Drawer", "drawer", DRAWER),
        DrawerRecipe
    )

    object AdornCategory : GeneratorInfo.Category {
        override val description = null
        override val displayName = "Adorn"
        override val placeholderTexturePath = null
    }

    enum class Subcategories(override val displayName: String, override val description: String? = null) : GeneratorInfo.Subcategory {
        Sofas("Sofas"),
        Chairs("Chairs"),
        Tables("Tables"),
        Kitchen("Kitchen"),
        Drawers("Drawer")
    }

    @JvmStatic
    fun main(args: Array<String>) {
        io.github.cottonmc.jsonfactory.gui.main(
            arrayOf(AdornPlugin::class.qualifiedName!!)
        )
    }
}
