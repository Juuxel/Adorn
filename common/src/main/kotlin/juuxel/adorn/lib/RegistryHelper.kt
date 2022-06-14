package juuxel.adorn.lib

import juuxel.adorn.item.BaseBlockItem
import juuxel.adorn.platform.PlatformBridges
import juuxel.adorn.platform.Registrar
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.registry.Registry

abstract class RegistryHelper {
    val blocks: Registrar<Block> = PlatformBridges.registrarFactory.create(Registry.BLOCK_KEY)
    val items: Registrar<Item> = PlatformBridges.registrarFactory.create(Registry.ITEM_KEY)

    // ----------------------------------
    // Functions for registering blocks
    // ----------------------------------

    /**
     * Registers a [block] with the [name] and an item in the [itemGroup].
     */
    protected fun <T : Block> registerBlock(name: String, itemGroup: ItemGroup = ItemGroup.DECORATIONS, block: () -> T): Registered<T> =
        registerBlock(name, itemSettings = { Item.Settings().group(itemGroup) }, block)

    /**
     * Registers a [block] with the [name] and the [itemSettings].
     */
    protected fun <T : Block> registerBlock(name: String, itemSettings: () -> Item.Settings, block: () -> T): Registered<T> =
        registerBlock(name, itemProvider = { makeItemForBlock(it, itemSettings()) }, block)

    /**
     * Registers a [block] with the [name] and an item created by the [itemProvider].
     */
    protected fun <T : Block> registerBlock(name: String, itemProvider: (T) -> Item, block: () -> T): Registered<T> {
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
        BaseBlockItem(block, itemSettings)

    // -----------------------------------------
    // Functions for registering other content
    // -----------------------------------------

    protected fun <T : Item> registerItem(name: String, content: () -> T): Registered<T> =
        items.register(name, content)
}
