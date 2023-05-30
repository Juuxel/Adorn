package juuxel.adorn.compat.emi

import com.mojang.blaze3d.systems.RenderSystem
import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import juuxel.adorn.AdornCommon
import juuxel.adorn.client.gui.screen.BrewerScreen
import juuxel.adorn.item.AdornItems
import juuxel.adorn.recipe.FluidBrewingRecipe
import juuxel.adorn.recipe.ItemBrewingRecipe
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.render.GameRenderer
import net.minecraft.util.Identifier
import kotlin.math.roundToInt

class BrewingEmiRecipe private constructor(
    private val id: Identifier,
    private val inputItem: EmiIngredient,
    private val firstItemIngredient: EmiIngredient,
    private val secondItemIngredient: EmiIngredient,
    private val fluidIngredient: EmiIngredient,
    private val result: EmiStack
) : EmiRecipe {
    constructor(recipe: ItemBrewingRecipe) : this(
        recipe.id,
        EmiStack.of(AdornItems.MUG),
        EmiIngredient.of(recipe.firstIngredient).withRemainders(),
        EmiIngredient.of(recipe.secondIngredient).withRemainders(),
        EmiStack.EMPTY,
        EmiStack.of(recipe.result)
    )

    constructor(recipe: FluidBrewingRecipe) : this(
        recipe.id,
        EmiStack.of(AdornItems.MUG),
        EmiIngredient.of(recipe.firstIngredient).withRemainders(),
        EmiIngredient.of(recipe.secondIngredient).withRemainders(),
        recipe.fluid.toEmiIngredient(),
        EmiStack.of(recipe.result)
    )

    override fun getCategory(): EmiRecipeCategory = AdornEmiPlugin.BREWER_CATEGORY
    override fun getId(): Identifier = id

    override fun getInputs() = listOf(
        inputItem,
        firstItemIngredient,
        secondItemIngredient,
        fluidIngredient,
    )

    override fun getOutputs() = listOf(result)
    override fun getDisplayWidth(): Int = 78 + 27 + 2 * PADDING
    override fun getDisplayHeight(): Int = 61 + 2 * PADDING

    override fun addWidgets(widgets: WidgetHolder) {
        val leftX = PADDING
        val topY = PADDING
        widgets.addTexture(TEXTURE, leftX, topY, 105, 61, 49, 16)
        widgets.addSlot(firstItemIngredient, leftX, topY).drawBack(false)
        widgets.addSlot(secondItemIngredient, leftX + 60, topY).drawBack(false)
        widgets.addSlot(result, leftX + 26, topY + 35).drawBack(false).recipeContext(this).large(true)
        widgets.add(FluidSlotWidget(fluidIngredient, leftX + 87, topY, 18, BrewerScreen.FLUID_AREA_HEIGHT + 2))

        // Empty mug
        widgets.addSlot(inputItem, leftX + 3, topY + 38).drawBack(false)

        // Fluid scale
        widgets.addDrawable(leftX + 88, topY + 5, 16, 51) { matrices, _, _, _ ->
            RenderSystem.setShaderTexture(0, TEXTURE)
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
            RenderSystem.setShader(GameRenderer::getPositionTexProgram)
            DrawableHelper.drawTexture(matrices, 0, 0, FLUID_SCALE_Z_OFFSET, 176f, 25f, 16, 51, 256, 256)
        }

        // Progress arrow
        widgets.addDrawable(leftX + 35, topY + 8, 8, BrewerScreen.FLUID_AREA_HEIGHT) { matrices, _, _, _ ->
            val progressFraction = (System.currentTimeMillis() % 4000) / 4000.0
            val height = (progressFraction * 25).roundToInt()
            DrawableHelper.drawTexture(matrices, 0, 0, 1, 176f, 0f, 8, height, 256, 256)
        }
    }

    companion object {
        private const val PADDING = 0
        private const val FLUID_SCALE_Z_OFFSET = 100
        private val TEXTURE = AdornCommon.id("textures/gui/brewer_emi.png")
    }
}
