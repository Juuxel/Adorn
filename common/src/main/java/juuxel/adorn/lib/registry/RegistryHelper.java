package juuxel.adorn.lib.registry;

import juuxel.adorn.item.BaseBlockItem;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.util.function.BiFunction;
import java.util.function.Function;

public final class RegistryHelper {
    private final Registrar<Block> blocks;
    private final Registrar<Item> items;

    public RegistryHelper(Registrar<Block> blocks, Registrar<Item> items) {
        this.blocks = blocks;
        this.items = items;
    }

    // ----------------------------------
    // Functions for registering blocks
    // ----------------------------------

    /**
     * Registers a block with the name and an item with default settings.
     */
    public <T extends Block> Registered<T> registerBlock(String name, float exchangeValue, Function<AbstractBlock.Settings, T> block, BlockSettingsProvider settings) {
        return registerBlock(name, ItemSettingsProvider.DEFAULT, exchangeValue, block, settings);
    }

    /**
     * Registers a block with the name and the item settings.
     */
    public <T extends Block> Registered<T> registerBlock(
        String name,
        ItemSettingsProvider itemSettings,
        float exchangeValue,
        Function<AbstractBlock.Settings, T> block,
        BlockSettingsProvider settings
    ) {
        return registerBlock(name, RegistryHelper::makeItemForBlock, itemSettings, exchangeValue, block, settings);
    }

    /**
     * Registers a block with the name and an item created by the item provider with default settings.
     */
    public <T extends Block> Registered<T> registerBlock(
        String name,
        BiFunction<T, Item.Settings, Item> itemProvider,
        float exchangeValue,
        Function<AbstractBlock.Settings, T> block,
        BlockSettingsProvider settings
    ) {
        return registerBlock(name, itemProvider, ItemSettingsProvider.DEFAULT, exchangeValue, block, settings);
    }

    /**
     * Registers a block with the name and an item created by the item provider.
     */
    public <T extends Block> Registered<T> registerBlock(
        String name,
        BiFunction<T, Item.Settings, Item> itemProvider,
        ItemSettingsProvider itemSettings,
        float exchangeValue,
        Function<AbstractBlock.Settings, T> block,
        BlockSettingsProvider settings
    ) {
        var registered = registerBlockWithoutItem(name, block, settings);
        items.register(name, key -> itemProvider.apply(
            registered.get(),
            itemSettings.createItemSettings()
                .registryKey(key)
                .useBlockPrefixedTranslationKey()
                .exchangeValue(exchangeValue)
        ));
        return registered;
    }

    /**
     * Registers a block with the name and without an item.
     */
    public <T extends Block> Registered<T> registerBlockWithoutItem(String name, Function<AbstractBlock.Settings, T> block, BlockSettingsProvider settings) {
        return blocks.register(name, key -> block.apply(settings.createBlockSettings().registryKey(key)));
    }

    private static Item makeItemForBlock(Block block, Item.Settings itemSettings) {
        return new BaseBlockItem(block, itemSettings);
    }

    // -----------------------------------------
    // Functions for registering other content
    // -----------------------------------------

    public <T extends Item> Registered<T> registerItem(String name, Function<Item.Settings, T> factory) {
        return registerItem(name, factory, ItemSettingsProvider.DEFAULT);
    }

    public <T extends Item> Registered<T> registerItem(String name, Function<Item.Settings, T> factory, ItemSettingsProvider settings) {
        return items.register(name, key -> factory.apply(settings.createItemSettings().registryKey(key)));
    }

    @FunctionalInterface
    public interface BlockSettingsProvider {
        AbstractBlock.Settings createBlockSettings();
    }

    @FunctionalInterface
    public interface ItemSettingsProvider {
        ItemSettingsProvider DEFAULT = Item.Settings::new;

        Item.Settings createItemSettings();
    }
}
