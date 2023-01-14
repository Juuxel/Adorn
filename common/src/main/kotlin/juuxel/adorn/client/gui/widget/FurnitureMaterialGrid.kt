package juuxel.adorn.client.gui.widget

import com.mojang.blaze3d.systems.RenderSystem
import juuxel.adorn.AdornCommon
import juuxel.adorn.client.resources.BlockVariantIcon
import juuxel.adorn.client.resources.BlockVariantTextureLoader
import juuxel.adorn.design.FurniturePartMaterial
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder
import net.minecraft.client.gui.tooltip.Tooltip
import net.minecraft.client.gui.widget.PressableWidget
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text

object FurnitureMaterialGrid {
    private const val WIDTH = 4
    private const val HEIGHT = 4
    private const val CELL_SIZE = 20
    private const val CELL_GAP = 2
    private val TEXTURE = AdornCommon.id("textures/gui/furniture_workbench_widgets.png")

    private fun createPage(x: Int, y: Int, materials: List<FurniturePartMaterial>, start: Int, materialListener: (FurniturePartMaterial) -> Unit): Panel {
        val panel = Panel()

        for (i in start until (start + WIDTH * HEIGHT)) {
            if (i > materials.lastIndex) break

            val row = (i - start) / WIDTH
            val col = (i - start) % WIDTH
            val buttonX = x + col * (CELL_SIZE + CELL_GAP)
            val buttonY = y + row * (CELL_SIZE + CELL_GAP)
            panel.add(MaterialButton(buttonX, buttonY, materials[i], materialListener))
        }

        return panel
    }

    fun createFlipBook(x: Int, y: Int, pageUpdateListener: () -> Unit, materialListener: (FurniturePartMaterial) -> Unit): FlipBook {
        val flipBook = FlipBook(pageUpdateListener)
        val materials = FurniturePartMaterial.ALL

        for (start in materials.indices step (WIDTH * HEIGHT)) {
            flipBook.add(createPage(x, y, materials, start, materialListener))
        }

        return flipBook
    }

    // TODO: names for block variants
    private class MaterialButton(x: Int, y: Int, private val material: FurniturePartMaterial, private val materialListener: (FurniturePartMaterial) -> Unit)
        : PressableWidget(x, y, CELL_SIZE, CELL_SIZE, Text.literal("TODO")) {
        private val itemRenderer = MinecraftClient.getInstance().itemRenderer

        init {
            setTooltip(Tooltip.of(material.displayName))
        }

        override fun renderButton(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
            RenderSystem.setShaderTexture(0, TEXTURE)
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
            val v = if (isHovered) 20f else 0f
            drawTexture(matrices, x, y, 20f, v, width, height, 64, 64)
            val icon = BlockVariantTextureLoader.get(material.id)?.icon ?: BlockVariantIcon.MISSING
            icon.render(matrices, x + 2, y + 2, itemRenderer)
        }

        override fun appendClickableNarrations(builder: NarrationMessageBuilder) {
            appendDefaultNarrations(builder)
        }

        override fun onPress() {
            materialListener(material)
        }
    }
}
