package juuxel.adorn.compat.rei.client

import com.mojang.blaze3d.systems.RenderSystem
import juuxel.adorn.AdornCommon
import juuxel.adorn.block.AdornBlocks
import juuxel.adorn.client.gui.screen.BrewerScreen
import juuxel.adorn.compat.rei.AdornReiServer
import juuxel.adorn.compat.rei.BrewerDisplay
import me.shedaniel.math.Point
import me.shedaniel.math.Rectangle
import me.shedaniel.rei.api.client.gui.Renderer
import me.shedaniel.rei.api.client.gui.widgets.Widget
import me.shedaniel.rei.api.client.gui.widgets.Widgets
import me.shedaniel.rei.api.client.registry.display.DisplayCategory
import me.shedaniel.rei.api.common.util.EntryStacks
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import kotlin.math.roundToInt

// TODO: Dark mode, see REIRuntime
class BrewerCategory : DisplayCategory<BrewerDisplay> {
    override fun getIcon(): Renderer = EntryStacks.of(AdornBlocks.BREWER)
    override fun getTitle(): Text = TranslatableText("category.adorn.brewer")
    override fun getCategoryIdentifier() = AdornReiServer.BREWER
    override fun getDisplayHeight(): Int = 72

    override fun setupDisplay(display: BrewerDisplay, bounds: Rectangle): List<Widget> = buildList {
        val topLeft = Point(bounds.centerX - 39, bounds.centerY - 30)
        add(Widgets.createRecipeBase(bounds))
        add(
            Widgets.createDrawableWidget { helper, matrices, mouseX, mouseY, delta ->
                RenderSystem.setShaderTexture(0, TEXTURE)
                RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
                helper.drawTexture(matrices, topLeft.x, topLeft.y, 49, 16, 105, 61)
                val progressFraction = (System.currentTimeMillis() % 4000) / 4000.0
                val height = (progressFraction * 25).roundToInt()
                helper.drawTexture(matrices, topLeft.x + 35, topLeft.y + 8, 176, 0, 8, height)
            }
        )
        add(
            Widgets.createSlot(Point(topLeft.x + 1, topLeft.y + 1))
                .disableBackground()
                .markInput()
                .entries(display.first)
        )
        add(
            Widgets.createSlot(Point(topLeft.x + 61, topLeft.y + 1))
                .disableBackground()
                .markInput()
                .entries(display.second)
        )
        add(
            Widgets.createSlot(Point(topLeft.x + 31, topLeft.y + 40))
                .disableBackground()
                .markOutput()
                .entry(display.result)
        )
        add(
            Widgets.createSlot(Rectangle(topLeft.x + 87, topLeft.y, 18, BrewerScreen.FLUID_AREA_HEIGHT + 2))
                .disableBackground()
                .markInput()
                .entries(display.fluid)
        )
        // Empty mug, must have background since it's not in the screen texture
        add(
            Widgets.createSlot(Point(topLeft.x + 4, topLeft.y + 39))
                .markInput()
                .entries(display.input)
        )
        // Fluid scale for empty fluid slots
        add(
            Widgets.createDrawableWidget { helper, matrices, mouseX, mouseY, delta ->
                RenderSystem.setShaderTexture(0, TEXTURE)
                RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
                helper.zOffset++
                helper.drawTexture(matrices, topLeft.x + 88, topLeft.y + 5, 176, 25, 16, 51)
                helper.zOffset--
            }
        )
    }

    companion object {
        private val TEXTURE = AdornCommon.id("textures/gui/brewer_rei.png")
    }
}
