package juuxel.adorn.lib

import juuxel.adorn.block.BlockWithDescription
import juuxel.adorn.block.entity.BETypeProvider
import juuxel.adorn.block.entity.MutableBlockEntityType
import juuxel.adorn.item.BaseBlockItem
import net.minecraft.block.Block
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.world.World

abstract class RegistryHelper(private val namespace: String) {
    protected fun <R> register(registry: Registry<in R>, name: String, content: R): R {
        return Registry.register(
            registry,
            Identifier(namespace, name),
            content
        )
    }

    //----------------------------------
    // Functions for registering blocks
    //----------------------------------

    /**
     * Registers a [block] with the [name] and an item in the [itemGroup].
     */
    protected fun <T : Block> registerBlock(name: String, block: T, itemGroup: ItemGroup = ItemGroup.DECORATIONS): T =
        registerBlock(name, block, Item.Settings().group(itemGroup))

    /**
     * Registers a [block] with the [name] and the [itemSettings].
     */
    protected fun <T : Block> registerBlock(name: String, block: T, itemSettings: Item.Settings): T =
        registerBlock(name, block) { makeItemForBlock(it, itemSettings) }

    /**
     * Registers a [block] with the [name] and an item created by the [itemProvider].
     */
    protected inline fun <T : Block> registerBlock(name: String, block: T, itemProvider: (T) -> Item): T {
        register(Registry.BLOCK, name, block)
        register(Registry.ITEM, name, itemProvider(block))
        if (block is BETypeProvider) {
            val type = block.blockEntityType
            if (!Registry.BLOCK_ENTITY_TYPE.contains(type)) {
                register(Registry.BLOCK_ENTITY_TYPE, name, type)
            }

            if (type is MutableBlockEntityType<*>) {
                type.addBlock(block)
            }
        }
        return block
    }

    /**
     * Registers a [block] with the [name] and without an item.
     */
    protected fun <T : Block> registerBlockWithoutItem(name: String, block: T): T =
        register(Registry.BLOCK, name, block)

    private fun makeItemForBlock(block: Block, itemSettings: Item.Settings): Item =
        if (block is BlockWithDescription) {
            object : BaseBlockItem(block, itemSettings) {
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
            BaseBlockItem(block, itemSettings)
        }

    //-----------------------------------------
    // Functions for registering other content
    //-----------------------------------------

    protected fun <T : Item> registerItem(name: String, content: T): T =
        register(Registry.ITEM, name, content)
}
