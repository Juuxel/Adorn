package juuxel.adorn.lib.registry;

import juuxel.adorn.block.BlockWithItemComponents;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceKey;

import java.util.function.BiFunction;
import java.util.function.Function;

public final class RegistryHelper {
    private final KeyedRegistrar<Block> blocks;
    private final KeyedRegistrar<Item> items;

    public RegistryHelper(KeyedRegistrar<Block> blocks, KeyedRegistrar<Item> items) {
        this.blocks = blocks;
        this.items = items;
    }

    // ----------------------------------
    // Functions for registering blocks
    // ----------------------------------

    /**
     * Registers a block with the name and an item with default settings.
     */
    public <T extends Block> Registered<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> block, BlockSettingsProvider settings) {
        return registerBlock(name, ItemSettingsProvider.DEFAULT, block, settings);
    }

    /**
     * Registers a block with the name and the item settings.
     */
    public <T extends Block> Registered<T> registerBlock(
        String name,
        ItemSettingsProvider itemSettings,
        Function<BlockBehaviour.Properties, T> block,
        BlockSettingsProvider settings
    ) {
        return registerBlock(name, RegistryHelper::makeItemForBlock, itemSettings, block, settings);
    }

    /**
     * Registers a block with the name and an item created by the item provider with default settings.
     */
    public <T extends Block> Registered<T> registerBlock(
        String name,
        BiFunction<T, net.minecraft.world.item.Item.Properties, Item> itemProvider,
        Function<BlockBehaviour.Properties, T> block,
        BlockSettingsProvider settings
    ) {
        return registerBlock(name, itemProvider, ItemSettingsProvider.DEFAULT, block, settings);
    }

    /**
     * Registers a block with the name and an item created by the item provider.
     */
    public <T extends Block> Registered<T> registerBlock(
        String name,
        BiFunction<T, net.minecraft.world.item.Item.Properties, Item> itemProvider,
        ItemSettingsProvider itemSettings,
        Function<BlockBehaviour.Properties, T> block,
        BlockSettingsProvider settings
    ) {
        var registered = registerBlockWithoutItem(name, block, settings);
        items.register(name, key -> itemProvider.apply(
            registered.get(),
            createBlockItemSettings(registered.get(), key, itemSettings)
        ));
        return registered;
    }

    private static net.minecraft.world.item.Item.Properties createBlockItemSettings(Block block, ResourceKey<Item> key, ItemSettingsProvider provider) {
        var settings = provider.createItemSettings()
            .setId(key)
            .useBlockDescriptionPrefix();

        if (block instanceof BlockWithItemComponents withItemComponents) {
            withItemComponents.addItemComponents(settings::component);
        }

        return settings;
    }

    /**
     * Registers a block with the name and without an item.
     */
    public <T extends Block> Registered<T> registerBlockWithoutItem(String name, Function<BlockBehaviour.Properties, T> block, BlockSettingsProvider settings) {
        return blocks.register(name, key -> block.apply(settings.createBlockSettings().setId(key)));
    }

    private static Item makeItemForBlock(Block block, net.minecraft.world.item.Item.Properties itemSettings) {
        return new BlockItem(block, itemSettings);
    }

    // -----------------------------------------
    // Functions for registering other content
    // -----------------------------------------

    public <T extends Item> Registered<T> registerItem(String name, Function<net.minecraft.world.item.Item.Properties, T> factory) {
        return registerItem(name, factory, ItemSettingsProvider.DEFAULT);
    }

    public <T extends Item> Registered<T> registerItem(String name, Function<net.minecraft.world.item.Item.Properties, T> factory, ItemSettingsProvider settings) {
        return items.register(name, key -> factory.apply(settings.createItemSettings().setId(key)));
    }

    @FunctionalInterface
    public interface BlockSettingsProvider {
        BlockBehaviour.Properties createBlockSettings();
    }

    @FunctionalInterface
    public interface ItemSettingsProvider {
        ItemSettingsProvider DEFAULT = net.minecraft.world.item.Item.Properties::new;

        net.minecraft.world.item.Item.Properties createItemSettings();
    }
}
