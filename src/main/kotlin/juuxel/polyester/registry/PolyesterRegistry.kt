package juuxel.polyester.registry

import juuxel.adorn.block.BlockWithDescription
import juuxel.polyester.block.PolyesterBlock
import juuxel.polyester.block.PolyesterBlockEntityType
import juuxel.polyester.item.PolyesterItem
import net.minecraft.block.Block
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeType
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.world.World

abstract class PolyesterRegistry(private val namespace: String) {
    protected fun <R, C : PolyesterContent<R>> register(registry: Registry<in R>, content: C): C {
        register(
            registry,
            content.name,
            content.unwrap()
        )
        return content
    }

    protected fun <R> register(registry: Registry<in R>, name: String, content: R): R {
        return Registry.register(
            registry,
            Identifier(namespace, name),
            content
        )
    }

    /**
     * Registers a [block] with the [name] and an item in the [itemGroup].
     */
    protected fun <T : Block> registerBlock(name: String, block: T, itemGroup: ItemGroup): T =
        registerBlock(name, block, Item.Settings().group(itemGroup))

    /**
     * Registers a [block] with the [name] and the [itemSettings].
     */
    protected fun <T : Block> registerBlock(name: String, block: T, itemSettings: Item.Settings): T {
        Registry.register(Registry.BLOCK, Identifier(namespace, name), block)
        Registry.register(Registry.ITEM, Identifier(namespace, name), makeItemForBlock(block, itemSettings))
        return block
    }

    private fun makeItemForBlock(block: Block, itemSettings: Item.Settings): Item =
        if (block is BlockWithDescription) {
            object : BlockItem(block, itemSettings) {
                override fun appendTooltip(
                    stack: ItemStack?, world: World?, texts: MutableList<Text>, context: TooltipContext?
                ) {
                    super.appendTooltip(stack, world, texts, context)
                    texts.add(TranslatableText(block.descriptionKey).styled {
                        it.isItalic = true
                        it.color = Formatting.DARK_GRAY
                    })
                }
            }
        } else {
            BlockItem(block, itemSettings)
        }

    protected fun <T : PolyesterBlock> registerBlock(content: T): T {
        register(Registry.BLOCK, content)

        if (content.itemSettings != null)
            register(
                Registry.ITEM,
                content.name,
                object : BlockItem(content.unwrap(), content.itemSettings),
                    PolyesterItem, HasDescription by content {
                    override val name = content.name

                    override fun appendTooltip(
                        stack: ItemStack?,
                        world: World?,
                        list: MutableList<Text>,
                        context: TooltipContext?
                    ) {
                        super.appendTooltip(stack, world, list, context)
                        PolyesterItem.appendTooltipToList(list, this)
                    }
                }
            )

        val blockEntityType = content.blockEntityType

        if (blockEntityType != null) {
            if (!Registry.BLOCK_ENTITY.contains(blockEntityType)) {
                register(
                    Registry.BLOCK_ENTITY,
                    content.name,
                    blockEntityType
                )
            }

            if (blockEntityType is PolyesterBlockEntityType<*>) {
                blockEntityType.addBlock(content.unwrap())
            }
        }

        return content
    }

    protected fun <R : Recipe<*>> registerRecipe(name: String): RecipeType<R> {
        return register(Registry.RECIPE_TYPE, object : RecipeType<R>,
            PolyesterContent<RecipeType<R>> {
            override val name = name
            override fun toString() = name
        })
    }

    protected fun <T : PolyesterItem> registerItem(content: T): T =
        register(Registry.ITEM, content)

    protected fun <T : Item> registerItem(name: String, content: T): T =
        register(Registry.ITEM, name, content)
}
