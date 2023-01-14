package juuxel.adorn.client.gui.widget

import com.mojang.blaze3d.systems.RenderSystem
import juuxel.adorn.AdornCommon
import juuxel.adorn.client.resources.BlockVariantIcon
import juuxel.adorn.client.resources.BlockVariantTextureLoader
import juuxel.adorn.design.FurniturePartMaterial
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder
import net.minecraft.client.gui.tooltip.Tooltip
import net.minecraft.client.gui.widget.PressableWidget
import net.minecraft.client.util.math.MatrixStack

object FurnitureMaterialGrid {
    private const val WIDTH = 4
    private const val HEIGHT = 4
    private const val CELL_SIZE = 20
    private const val CELL_GAP = 1
    private val TEXTURE = AdornCommon.id("textures/gui/furniture_workbench_widgets.png")

    private fun createPage(
        x: Int, y: Int,
        materials: List<FurniturePartMaterial>,
        start: Int,
        currentMaterial: () -> FurniturePartMaterial,
        materialListener: (FurniturePartMaterial) -> Unit
    ): Panel {
        val panel = Panel()

        for (i in start until (start + WIDTH * HEIGHT)) {
            if (i > materials.lastIndex) break

            val row = (i - start) / WIDTH
            val col = (i - start) % WIDTH
            val buttonX = x + col * (CELL_SIZE + CELL_GAP)
            val buttonY = y + row * (CELL_SIZE + CELL_GAP)
            panel.add(MaterialButton(buttonX, buttonY, materials[i], currentMaterial, materialListener))
        }

        return panel
    }

    fun createFlipBook(
        x: Int, y: Int,
        materials: List<FurniturePartMaterial>,
        pageUpdateListener: () -> Unit,
        currentMaterial: () -> FurniturePartMaterial,
        materialListener: (FurniturePartMaterial) -> Unit
    ): FlipBook<Element> {
        val flipBook = FlipBook<Element>(pageUpdateListener)

        for (start in materials.indices step (WIDTH * HEIGHT)) {
            flipBook.add(createPage(x, y, materials, start, currentMaterial, materialListener))
        }

        return flipBook
    }

    private class MaterialButton(
        x: Int, y: Int,
        private val material: FurniturePartMaterial,
        private val currentMaterial: () -> FurniturePartMaterial,
        private val materialListener: (FurniturePartMaterial) -> Unit
    ) : PressableWidget(x, y, CELL_SIZE, CELL_SIZE, material.displayName) {
        private val itemRenderer = MinecraftClient.getInstance().itemRenderer

        init {
            setTooltip(Tooltip.of(material.displayName))
        }

        override fun renderButton(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
            RenderSystem.setShaderTexture(0, TEXTURE)
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
            val u = if (currentMaterial() == material) 40 else 20
            val v = if (isHovered) 20 else 0
            drawTexture(matrices, x, y, u, v, width, height)
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
