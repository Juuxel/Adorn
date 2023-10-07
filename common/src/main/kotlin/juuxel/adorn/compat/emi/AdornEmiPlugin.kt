package juuxel.adorn.compat.emi

import dev.emi.emi.api.EmiEntrypoint
import dev.emi.emi.api.EmiPlugin
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiStack
import juuxel.adorn.AdornCommon
import juuxel.adorn.block.AdornBlocks
import juuxel.adorn.client.gui.screen.TradingStationScreen
import juuxel.adorn.recipe.AdornRecipes
import juuxel.adorn.recipe.FluidBrewingRecipe
import juuxel.adorn.recipe.ItemBrewingRecipe
import juuxel.adorn.util.logger

@EmiEntrypoint
class AdornEmiPlugin : EmiPlugin {
    override fun register(registry: EmiRegistry) {
        registry.addCategory(BREWER_CATEGORY)
        registry.addWorkstation(BREWER_CATEGORY, EmiStack.of(AdornBlocks.BREWER))

        val recipeManager = registry.recipeManager

        for (entry in recipeManager.listAllOfType(AdornRecipes.BREWING_TYPE)) {
            val emiRecipe = when (val recipe = entry.value) {
                is ItemBrewingRecipe -> BrewingEmiRecipe(entry.id, recipe)
                is FluidBrewingRecipe -> BrewingEmiRecipe(entry.id, recipe)
                else -> {
                    LOGGER.error("Unknown brewing recipe: {}", recipe)
                    continue
                }
            }

            registry.addRecipe(emiRecipe)
        }

        registry.addDragDropHandler(TradingStationScreen::class.java, TradingStationDragDropHandler)
    }

    companion object {
        private val LOGGER = logger()

        val BREWER_CATEGORY: EmiRecipeCategory = EmiRecipeCategory(
            AdornCommon.id("brewer"),
            EmiStack.of(AdornBlocks.BREWER),
            EmiTexture(AdornCommon.id("textures/gui/recipe_viewer/brewer_light.png"), 240, 0, 16, 16)
        )
    }
}
