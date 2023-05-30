package juuxel.adorn.compat.rei.client

import juuxel.adorn.block.AdornBlocks
import juuxel.adorn.compat.rei.AdornReiServer
import juuxel.adorn.compat.rei.BrewerDisplay
import juuxel.adorn.lib.AdornTags
import juuxel.adorn.recipe.AdornRecipes
import juuxel.adorn.recipe.FluidBrewingRecipe
import juuxel.adorn.recipe.ItemBrewingRecipe
import me.shedaniel.rei.api.client.plugins.REIClientPlugin
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry
import me.shedaniel.rei.api.client.registry.entry.CollapsibleEntryRegistry
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry
import me.shedaniel.rei.api.common.util.EntryIngredients
import me.shedaniel.rei.api.common.util.EntryStacks
import net.minecraft.item.Item
import net.minecraft.tag.TagKey
import net.minecraft.text.Text

open class AdornReiClient : REIClientPlugin {
    override fun registerCategories(registry: CategoryRegistry) {
        registry.add(BrewerCategory())
        registry.addWorkstations(AdornReiServer.BREWER, EntryStacks.of(AdornBlocks.BREWER))
    }

    override fun registerDisplays(registry: DisplayRegistry) {
        registry.registerRecipeFiller(ItemBrewingRecipe::class.java, AdornRecipes.BREWING_TYPE, ::BrewerDisplay)
        registry.registerRecipeFiller(FluidBrewingRecipe::class.java, AdornRecipes.BREWING_TYPE, ::BrewerDisplay)
    }

    override fun registerCollapsibleEntries(registry: CollapsibleEntryRegistry) {
        registry.add(AdornTags.SOFAS.item)
        registry.add(AdornTags.CHAIRS.item)
        registry.add(AdornTags.TABLES.item)
        registry.add(AdornTags.DRAWERS.item)
        registry.add(AdornTags.KITCHEN_COUNTERS.item)
        registry.add(AdornTags.KITCHEN_CUPBOARDS.item)
        registry.add(AdornTags.KITCHEN_SINKS.item)
        registry.add(AdornTags.POSTS.item)
        registry.add(AdornTags.PLATFORMS.item)
        registry.add(AdornTags.STEPS.item)
        registry.add(AdornTags.SHELVES.item)
        registry.add(AdornTags.CHIMNEYS.item)
        registry.add(AdornTags.COFFEE_TABLES.item)
        registry.add(AdornTags.BENCHES.item)
        registry.add(AdornTags.CRATES.item)
        registry.add(AdornTags.TABLE_LAMPS.item)
        registry.add(AdornTags.CANDLELIT_LANTERNS.item)
    }

    override fun registerScreens(registry: ScreenRegistry) {
        registry.registerDraggableStackVisitor(TradingStationDraggableStackVisitor)
    }

    private fun CollapsibleEntryRegistry.add(tag: TagKey<Item>) {
        val id = tag.id
        val name = Text.translatable("tag.${id.namespace}.${id.path}") // matches the translation keys used by EMI as well
        group(id, name, EntryIngredients.ofItemTag(tag))
    }
}
