package juuxel.adorn.lib

import juuxel.adorn.block.BlockWithDescription
import juuxel.adorn.item.BaseBlockItem
import juuxel.adorn.platform.PlatformBridges
import juuxel.adorn.platform.Registrar
import net.minecraft.block.Block
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Formatting
import net.minecraft.world.World

abstract class RegistryHelper() {
    val blocks: Registrar<Block> = PlatformBridges.registrarFactory.block()
    val items: Registrar<Item> = PlatformBridges.registrarFactory.item()

    // ----------------------------------
    // Functions for registering blocks
    // ----------------------------------

    /**
     * Registers a [block] with the [name] and an item in the [itemGroup].
     */
    protected fun <T : Block> registerBlock(name: String, itemGroup: ItemGroup = ItemGroup.DECORATIONS, block: () -> T): Registered<T> =
        registerBlock(name, itemSettings = { Item.Settings().group(itemGroup) }, block)

    // TODO: Check whether un-inlining this reduces jar size
    /**
     * Registers a [block] with the [name] and the [itemSettings].
     */
    protected inline fun <T : Block> registerBlock(name: String, crossinline itemSettings: () -> Item.Settings, noinline block: () -> T): Registered<T> =
        registerBlock(name, itemProvider = { makeItemForBlock(it, itemSettings()) }, block)

    // TODO: Check whether un-inlining this reduces jar size
    /**
     * Registers a [block] with the [name] and an item created by the [itemProvider].
     */
    protected inline fun <T : Block> registerBlock(name: String, crossinline itemProvider: (T) -> Item, noinline block: () -> T): Registered<T> {
        val registered = registerBlockWithoutItem(name, block)
        registerItem(name) { itemProvider(registered.get()) }
        return registered
    }

    /**
     * Registers a [block] with the [name] and without an item.
     */
    protected fun <T : Block> registerBlockWithoutItem(name: String, block: () -> T): Registered<T> =
        blocks.register(name, block)

    protected fun makeItemForBlock(block: Block, itemSettings: Item.Settings): Item =
        if (block is BlockWithDescription) {
            object : BaseBlockItem(block, itemSettings) {
                override fun appendTooltip(
                    stack: ItemStack?, world: World?, texts: MutableList<Text>, context: TooltipContext?
                ) {
                    super.appendTooltip(stack, world, texts, context)
                    texts.add(
                        TranslatableText(block.descriptionKey).styled {
                            it.withItalic(true).withColor(Formatting.DARK_GRAY)
                        }
                    )
                }
            }
        } else {
            BaseBlockItem(block, itemSettings)
        }

    // -----------------------------------------
    // Functions for registering other content
    // -----------------------------------------

    protected fun <T : Item> registerItem(name: String, content: () -> T): Registered<T> =
        items.register(name, content)
}
