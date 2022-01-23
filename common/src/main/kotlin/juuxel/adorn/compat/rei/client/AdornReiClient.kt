package juuxel.adorn.compat.rei.client

import juuxel.adorn.block.AdornBlocks
import juuxel.adorn.compat.rei.AdornReiServer
import juuxel.adorn.compat.rei.BrewerDisplay
import juuxel.adorn.recipe.AdornRecipes
import juuxel.adorn.recipe.BrewingRecipe
import me.shedaniel.rei.api.client.plugins.REIClientPlugin
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry
import me.shedaniel.rei.api.common.util.EntryStacks

open class AdornReiClient : REIClientPlugin {
    override fun registerCategories(registry: CategoryRegistry) {
        registry.add(BrewerCategory())
        registry.addWorkstations(AdornReiServer.BREWER, EntryStacks.of(AdornBlocks.BREWER))
        registry.removePlusButton(AdornReiServer.BREWER)
    }

    override fun registerDisplays(registry: DisplayRegistry) {
        registry.registerRecipeFiller(BrewingRecipe::class.java, AdornRecipes.BREWING_TYPE, ::BrewerDisplay)
    }
}
