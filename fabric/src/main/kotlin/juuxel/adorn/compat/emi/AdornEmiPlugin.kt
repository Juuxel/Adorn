package juuxel.adorn.compat.emi

import dev.emi.emi.api.EmiPlugin
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import juuxel.adorn.AdornCommon
import juuxel.adorn.block.AdornBlocks
import juuxel.adorn.fluid.FluidIngredient
import juuxel.adorn.fluid.FluidUnit
import juuxel.adorn.recipe.AdornRecipes
import juuxel.adorn.recipe.FluidBrewingRecipe
import juuxel.adorn.recipe.ItemBrewingRecipe
import juuxel.adorn.util.logger
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant

object AdornEmiPlugin : EmiPlugin {
    private val LOGGER = logger()
    val BREWER_CATEGORY: EmiRecipeCategory = EmiRecipeCategory(
        AdornCommon.id("brewer"),
        EmiStack.of(AdornBlocks.BREWER),
        EmiTexture(AdornCommon.id("textures/gui/brewer_emi.png"), 240, 0, 16, 16)
    )

    override fun register(registry: EmiRegistry) {
        registry.addCategory(BREWER_CATEGORY)
        registry.addWorkstation(BREWER_CATEGORY, EmiStack.of(AdornBlocks.BREWER))

        val recipeManager = registry.recipeManager

        for (recipe in recipeManager.listAllOfType(AdornRecipes.BREWING_TYPE)) {
            val emiRecipe = when (recipe) {
                is ItemBrewingRecipe -> BrewingEmiRecipe(recipe)
                is FluidBrewingRecipe -> BrewingEmiRecipe(recipe)
                else -> {
                    LOGGER.error("Unknown brewing recipe: {}", recipe)
                    continue
                }
            }

            registry.addRecipe(emiRecipe)
        }
    }

    fun toEmiIngredient(ingredient: FluidIngredient): EmiIngredient {
        val amount = FluidUnit.convert(ingredient.amount, ingredient.unit, FluidUnit.DROPLET)
        return EmiIngredient.of(
            ingredient.fluid.fluids.map {
                EmiStack.of(FluidVariant.of(it, ingredient.nbt), amount)
            }
        )
    }
}
