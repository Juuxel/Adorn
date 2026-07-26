package juuxel.adorn.compat.rei.client;

import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.compat.rei.AdornReiServer;
import juuxel.adorn.lib.AdornTags;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.entry.CollapsibleEntryRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.world.item.Item;
import net.minecraft.tags.TagKey;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Util;

public class AdornReiClient implements REIClientPlugin {
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new BrewerCategory());
        registry.addWorkstations(AdornReiServer.BREWER, EntryStacks.of(AdornBlocks.BREWER.get()));
    }

    @Override
    public void registerCollapsibleEntries(CollapsibleEntryRegistry registry) {
        add(registry, AdornTags.SOFAS.item());
        add(registry, AdornTags.CHAIRS.item());
        add(registry, AdornTags.TABLES.item());
        add(registry, AdornTags.DRAWERS.item());
        add(registry, AdornTags.KITCHEN_COUNTERS.item());
        add(registry, AdornTags.KITCHEN_CUPBOARDS.item());
        add(registry, AdornTags.KITCHEN_SINKS.item());
        add(registry, AdornTags.POSTS.item());
        add(registry, AdornTags.PLATFORMS.item());
        add(registry, AdornTags.STEPS.item());
        add(registry, AdornTags.SHELVES.item());
        add(registry, AdornTags.CHIMNEYS.item());
        add(registry, AdornTags.COFFEE_TABLES.item());
        add(registry, AdornTags.BENCHES.item());
        add(registry, AdornTags.CRATES.item());
        add(registry, AdornTags.TABLE_LAMPS.item());
        add(registry, AdornTags.CANDLELIT_LANTERNS.item());
        add(registry, AdornTags.COPPER_PIPES.item());
        add(registry, AdornTags.PAINTED_PLANKS.item());
        add(registry, AdornTags.PAINTED_WOOD_SLABS.item());
        add(registry, AdornTags.PAINTED_WOOD_STAIRS.item());
        add(registry, AdornTags.PAINTED_WOOD_FENCES.item());
        add(registry, AdornTags.PAINTED_WOOD_FENCE_GATES.item());
        add(registry, AdornTags.PAINTED_WOOD_PRESSURE_PLATES.item());
        add(registry, AdornTags.PAINTED_WOOD_BUTTONS.item());
    }

    private static void add(CollapsibleEntryRegistry registry, TagKey<Item> tag) {
        // matches the translation keys used by EMI as well
        var name = Component.translatable(Util.makeDescriptionId("tag.item", tag.location()));
        registry.group(tag.location(), name, EntryIngredients.ofItemTag(tag));
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerDraggableStackVisitor(new TradingStationDraggableStackVisitor());
    }
}
