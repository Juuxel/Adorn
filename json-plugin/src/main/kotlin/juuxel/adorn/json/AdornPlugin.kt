package juuxel.adorn.json

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.frontend.i18n.I18n
import io.github.cottonmc.jsonfactory.frontend.i18n.ResourceBundleI18n
import io.github.cottonmc.jsonfactory.gens.ContentGenerator
import io.github.cottonmc.jsonfactory.gens.GeneratorInfo
import io.github.cottonmc.jsonfactory.gens.block.SuffixedBlockItemModel
import io.github.cottonmc.jsonfactory.gens.block.SuffixedBlockState
import io.github.cottonmc.jsonfactory.gens.block.SuffixedLootTable
import io.github.cottonmc.jsonfactory.plugin.Plugin
import juuxel.adorn.json.chair.*
import juuxel.adorn.json.chair.ChairBlockModel
import juuxel.adorn.json.chair.ChairItemModel
import juuxel.adorn.json.drawer.DrawerBlockModel
import juuxel.adorn.json.drawer.DrawerBlockState
import juuxel.adorn.json.drawer.DrawerRecipe
import juuxel.adorn.json.kitchen.*
import juuxel.adorn.json.kitchen.KitchenCupboardItemModel
import juuxel.adorn.json.post.StonePostBlockModel
import juuxel.adorn.json.platform.PlatformRecipe
import juuxel.adorn.json.platform.StonePlatformBlockModel
import juuxel.adorn.json.platform.WoodenPlatformBlockModel
import juuxel.adorn.json.post.StonePostRecipe
import juuxel.adorn.json.post.WoodenPostBlockModel
import juuxel.adorn.json.post.WoodenPostRecipe
import juuxel.adorn.json.sofa.SofaBlockModel
import juuxel.adorn.json.sofa.SofaBlockState
import juuxel.adorn.json.sofa.SofaItemModel
import juuxel.adorn.json.sofa.SofaRecipe
import juuxel.adorn.json.step.StoneStepBlockModel
import juuxel.adorn.json.step.StoneStepRecipe
import juuxel.adorn.json.step.WoodenStepBlockModel
import juuxel.adorn.json.step.WoodenStepRecipe
import juuxel.adorn.json.table.TableBlockModel
import juuxel.adorn.json.table.TableBlockState
import juuxel.adorn.json.table.TableItemModel
import juuxel.adorn.json.table.TableRecipe

object AdornPlugin : Plugin {
    //adorn:red,adorn:black,adorn:green,adorn:brown,adorn:blue,adorn:purple,adorn:cyan,adorn:light_gray,adorn:gray,adorn:pink,adorn:lime,adorn:yellow,adorn:light_blue,adorn:magenta,adorn:orange,adorn:white
    //adorn:oak,adorn:spruce,adorn:birch,adorn:jungle,adorn:acacia,adorn:dark_oak
    //adorn:stone,adorn:cobblestone,adorn:sandstone,adorn:andesite,adorn:granite,adorn:diorite

    private val planksItem = fun(id: Identifier) = Identifier.mc(id.path + "_planks")
    private val slabItem = fun(id: Identifier) = Identifier.mc(id.path + "_slab")
    private val woolItem = fun(id: Identifier) = Identifier.mc(id.path + "_wool")
    private val selfItem = fun(id: Identifier) = Identifier.mc(id.path)

    private fun constItem(const: Identifier) = fun(_: Identifier) = const

    override val i18n = ResourceBundleI18n("assets.adorn.lang")

    val SOFA = GeneratorInfo(
        AdornCategory,
        AdornPlugin.Subcategories.Sofas
    )
    val CHAIR = GeneratorInfo(
        AdornCategory,
        AdornPlugin.Subcategories.Chairs
    )
    val TABLE = GeneratorInfo(
        AdornCategory,
        AdornPlugin.Subcategories.Tables
    )
    val KITCHEN = GeneratorInfo(
        AdornCategory,
        AdornPlugin.Subcategories.Kitchen
    )
    val DRAWER = GeneratorInfo(
        AdornCategory,
        AdornPlugin.Subcategories.Drawers
    )

    val POST = GeneratorInfo(
        AdornCategory,
        AdornPlugin.Subcategories.Posts
    )
    val PLATFORM = GeneratorInfo(
        AdornCategory,
        AdornPlugin.Subcategories.Platforms
    )
    val STEP = GeneratorInfo(
        AdornCategory,
        AdornPlugin.Subcategories.Steps
    )

    val OTHER = GeneratorInfo(
        AdornCategory,
        AdornPlugin.Subcategories.Other
    )

    override val generators: List<ContentGenerator> = listOf(
        SofaBlockModel,
        SofaBlockState,
        SofaItemModel,
        SuffixedLootTable("sofa", SOFA),
        SofaRecipe,
        SuffixedRecipeAdvancementGenerator(
            "sofa.recipe_advancement",
            SOFA,
            "sofa",
            keyItems = listOf(woolItem)
        ),

        ChairBlockModel,
        ChairBlockState,
        ChairItemModel,
        ChairLootTable,
        ChairRecipe,
        SuffixedRecipeAdvancementGenerator(
            "chair.recipe_advancement",
            CHAIR,
            "chair",
            keyItems = listOf(slabItem)
        ),

        TableBlockModel,
        TableBlockState,
        TableItemModel,
        SuffixedLootTable("table", TABLE),
        TableRecipe,
        SuffixedRecipeAdvancementGenerator(
            "table.recipe_advancement",
            TABLE,
            "table",
            keyItems = listOf(slabItem)
        ),

        KitchenCounterBlockModel,
        KitchenCounterBlockState,
        SuffixedBlockItemModel("kitchen_counter", KITCHEN),
        SuffixedLootTable("kitchen_counter", KITCHEN),
        KitchenCounterRecipe,
        SuffixedRecipeAdvancementGenerator(
            "kitchen_counter.recipe_advancement",
            KITCHEN,
            "kitchen_counter",
            keyItems = listOf(planksItem)
        ),

        KitchenCupboardBlockModel,
        KitchenCupboardBlockState,
        KitchenCupboardItemModel,
        SuffixedLootTable("kitchen_cupboard", KITCHEN),
        KitchenCupboardRecipe,
        SuffixedRecipeAdvancementGenerator(
            "kitchen_cupboard.recipe_advancement",
            KITCHEN,
            "kitchen_cupboard",
            keyItems = listOf(planksItem, { it.suffixPath("_kitchen_counter") })
        ),

        DrawerBlockModel,
        DrawerBlockState,
        SuffixedBlockItemModel("drawer", DRAWER),
        SuffixedLootTable("drawer", DRAWER),
        DrawerRecipe,
        SuffixedRecipeAdvancementGenerator(
            "drawer.recipe_advancement",
            DRAWER,
            "drawer",
            keyItems = listOf(slabItem)
        ),

        RecipeAdvancementGenerator(
            "trading_station.recipe_advancement",
            OTHER,
            keyItems = listOf(constItem(Identifier.mc("emerald")))
        ),

        WoodenPostBlockModel,
        StonePostBlockModel,
        WoodenPostRecipe,
        StonePostRecipe,
        SuffixedBlockState("post", POST),
        SuffixedBlockItemModel("post", POST),
        SuffixedLootTable("post", POST),
        SuffixedRecipeAdvancementGenerator(
            "post.wooden.recipe_advancement",
            POST,
            "post",
            keyItems = listOf(planksItem)
        ),
        SuffixedRecipeAdvancementGenerator(
            "post.stone.recipe_advancement",
            POST,
            "post",
            keyItems = listOf(selfItem)
        ),

        WoodenPlatformBlockModel,
        StonePlatformBlockModel,
        SuffixedBlockState("platform", PLATFORM),
        SuffixedBlockItemModel("platform", PLATFORM),
        SuffixedLootTable("platform", PLATFORM),
        PlatformRecipe,
        SuffixedLootTable("platform", PLATFORM),
        SuffixedRecipeAdvancementGenerator(
            "platform.recipe_advancement",
            PLATFORM,
            "platform",
            keyItems = listOf(slabItem)
        ),

        WoodenStepBlockModel,
        StoneStepBlockModel,
        SuffixedBlockState("step", STEP),
        SuffixedBlockItemModel("step", STEP),
        SuffixedLootTable("step", STEP),
        WoodenStepRecipe,
        StoneStepRecipe,
        SuffixedLootTable("step", STEP),
        SuffixedRecipeAdvancementGenerator(
            "step.recipe_advancement",
            STEP,
            "step",
            keyItems = listOf(slabItem)
        )
    )

    object AdornCategory : GeneratorInfo.Category {
        override val id = "categories.adorn"
        override val placeholderTexturePath = null
    }

    enum class Subcategories(override val id: String) : GeneratorInfo.Subcategory {
        Sofas("subcategories.sofas"),
        Chairs("subcategories.chairs"),
        Tables("subcategories.tables"),
        Kitchen("subcategories.kitchen"),
        Drawers("subcategories.drawers"),
        Posts("subcategories.posts"),
        Platforms("subcategories.platforms"),
        Steps("subcategories.steps"),
        Other("subcategories.other"),
    }

    @JvmStatic
    fun main(args: Array<String>) {
        io.github.cottonmc.jsonfactory.gui.main(
            arrayOf("--plugin-classes", AdornPlugin::class.qualifiedName!!)
        )
    }
}
