package juuxel.adorn.client.gui.screen

import com.mojang.blaze3d.systems.RenderSystem
import juuxel.adorn.AdornCommon
import juuxel.adorn.block.variant.BlockVariant
import juuxel.adorn.block.variant.BlockVariantGroup
import juuxel.adorn.block.variant.BlockVariantSets
import juuxel.adorn.client.gui.TextRendering
import juuxel.adorn.client.gui.widget.FlipBook
import juuxel.adorn.client.gui.widget.FurnitureMaterialGrid
import juuxel.adorn.client.gui.widget.TabbedPane
import juuxel.adorn.client.gui.Icon
import juuxel.adorn.client.renderer.design.FurnitureDesignRenderer
import juuxel.adorn.client.resources.BlockVariantTextureLoader
import juuxel.adorn.design.FurniturePart
import juuxel.adorn.design.FurniturePartMaterial
import juuxel.adorn.menu.FurnitureWorkbenchMenu
import juuxel.adorn.util.Colors
import juuxel.adorn.util.Geometry
import juuxel.adorn.util.animation.AnimatedProperty
import juuxel.adorn.util.animation.AnimationEngine
import juuxel.adorn.util.animation.Interpolator
import juuxel.adorn.util.color
import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.Selectable
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.PressableWidget
import net.minecraft.client.render.BufferRenderer
import net.minecraft.client.render.DiffuseLighting
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.LightmapTextureManager
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenTexts
import net.minecraft.text.Text
import net.minecraft.util.math.MathHelper
import org.joml.Quaternionf
import org.joml.Vector3d
import org.joml.Vector3f
import org.joml.Vector4f
import kotlin.math.min
import kotlin.random.Random

class FurnitureWorkbenchScreen(menu: FurnitureWorkbenchMenu, playerInventory: PlayerInventory, title: Text) :
    AdornMenuScreen<FurnitureWorkbenchMenu>(menu, playerInventory, title) {
    private var currentMaterial: FurniturePartMaterial = FurniturePartMaterial.of(BlockVariant.OAK)
    private var focusedPart: FurniturePart? = null
    private lateinit var materialTabs: TabbedPane<FlipBook<Element>>
    private lateinit var previousPageButton: PageButton
    private lateinit var nextPageButton: PageButton
    private val animationEngine = AnimationEngine()

    override fun init() {
        super.init()
        addDrawableChild(DesignView(x, y))
        val button = ButtonWidget.builder(Text.literal("Add Cuboid")) {
            val x = Random.nextInt(0, 16)
            val y = Random.nextInt(0, 16)
            val z = Random.nextInt(0, 16)
            val width = Random.nextInt(1, 16 - x + 1)
            val height = Random.nextInt(1, 16 - y + 1)
            val depth = Random.nextInt(1, 16 - z + 1)
            menu.furnitureParts += FurniturePart(
                Vector3d(x.toDouble() + width / 2, y.toDouble() + height / 2, z.toDouble() + depth / 2),
                MathHelper.ceil(width * 0.5), MathHelper.ceil(height * 0.5), MathHelper.ceil(depth * 0.5),
                currentMaterial
            )
            menu.partChannel.pushToServer()
        }
            .position(5, y)
            .build()
        addDrawableChild(button)
        materialTabs = addDrawableChild(
            TabbedPane(5, y + 25, 115, 166) {
                topBottomTabs()

                for (group in BlockVariantGroup.values()) {
                    val variants = BlockVariantSets.allVariantsByGroup().get(group)
                    val materials = buildList {
                        for (variant in variants) {
                            add(FurniturePartMaterial.of(variant))
                        }

                        if (group == BlockVariantGroup.FUNCTIONAL) {
                            addAll(FurniturePartMaterial.Functional.values())
                        }
                    }
                    if (materials.isEmpty()) continue

                    val icon = BlockVariantTextureLoader.get(materials.first().id)?.icon ?: Icon.MISSING
                    tab(icon, group.displayName) { x, y ->
                        FurnitureMaterialGrid.createFlipBook(
                            x + 9, y + 13,
                            materials,
                            { updatePageButtons() },
                            { currentMaterial },
                            { updateCurrentMaterial(it) }
                        )
                    }
                }
            }
        )
        materialTabs.flipBook.addPageUpdateListener { updatePageButtons() }
        previousPageButton = addDrawableChild(PageButton(5 + 16, y + TabbedPane.TAB_HEIGHT + 27, false))
        nextPageButton = addDrawableChild(PageButton(5 + 16 + 83 - 12, y + TabbedPane.TAB_HEIGHT + 27, true))
        updatePageButtons()
        animationEngine.start()
    }

    private fun updatePageButtons() {
        previousPageButton.visible = materialTabs.flipBook.currentPageValue.hasPreviousPage()
        nextPageButton.visible = materialTabs.flipBook.currentPageValue.hasNextPage()
    }

    private fun updateCurrentMaterial(material: FurniturePartMaterial) {
        currentMaterial = material
        focusedPart?.material = material
        menu.partChannel.pushToServer()
    }

    override fun drawBackground(matrices: MatrixStack, delta: Float, mouseX: Int, mouseY: Int) {
    }

    override fun renderExtraForeground(matrices: MatrixStack, mouseX: Int, mouseY: Int, tickDelta: Float) {
        val currentTab = materialTabs.currentTab() ?: return
        TextRendering.drawCentered(matrices, textRenderer, currentTab.label, 62, y + TabbedPane.TAB_HEIGHT + 29, Colors.SCREEN_TEXT)
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
        super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)

        if (button == 0 && isDragging) {
            focused?.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
        }

        return true
    }

    private fun drawCompass(matrices: MatrixStack) {
        DiffuseLighting.enableGuiDepthLighting()
        RenderSystem.setShader(GameRenderer::getPositionTexProgram)
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        RenderSystem.setShaderTexture(0, WIDGETS)

        val px = 1 / 256f
        val position = matrices.peek().positionMatrix
        val buffer = Tessellator.getInstance().buffer
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE)
        buffer.vertex(position, -2f, 0f, -6f).texture(20 * px, 24 * px).next()
        buffer.vertex(position, -2f, 0f, 18f).texture(20 * px, 0f).next()
        buffer.vertex(position, 18f, 0f, 18f).texture(0f, 0f).next()
        buffer.vertex(position, 18f, 0f, -6f).texture(0f, 24 * px).next()
        BufferRenderer.drawWithGlobalProgram(buffer.end())

        DiffuseLighting.disableGuiDepthLighting()
    }

    override fun removed() {
        super.removed()
        animationEngine.stop()
    }

    companion object {
        private const val DESIGN_AREA_WIDTH = 8 * 18
        private const val DESIGN_AREA_HEIGHT = 5 * 18

        // Rotation speeds in radians per pixel
        private const val ROTATION_SPEED_AROUND_Y = 0.03
        private const val HORIZONTAL_ROTATION_SPEED = 0.03
        private const val MIN_ZOOM = 1f
        private const val MAX_ZOOM = 5f
        private val WIDGETS = AdornCommon.id("textures/gui/furniture_workbench_widgets.png")
    }

    private inner class DesignView(private val x: Int, private val y: Int) : Drawable, Element, Selectable {
        private var focused: Boolean = false
        private var hovered: Boolean = false

        private var zoom by AnimatedProperty(3f, animationEngine, 20, Interpolator.FLOAT)
        private var rotationY = MathHelper.PI
        private var rotationXZ = MathHelper.PI * 0.1f

        private fun applyFurnitureTransforms(matrices: MatrixStack) {
            matrices.translate(x + 0.5f * DESIGN_AREA_WIDTH, y + 0.5f * DESIGN_AREA_HEIGHT, 100f)
            matrices.scale(zoom, -zoom, zoom)
            matrices.translate(0f, 0f, 8f)
            val quaternion = Quaternionf().rotateY(rotationY)
            matrices.multiply(quaternion)
            quaternion.identity().rotateY(-rotationY)
            val horizontalAxis = Vector3f(1f, 0f, 0f).rotate(quaternion)
            matrices.multiply(quaternion.rotationAxis(rotationXZ, horizontalAxis))
            matrices.translate(-8f, -8f, -8f)
        }

        override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
            hovered = isMouseOver(mouseX.toDouble(), mouseY.toDouble())

            DrawableHelper.enableScissor(x, y, x + DESIGN_AREA_WIDTH, y + DESIGN_AREA_HEIGHT)
            RenderSystem.enableDepthTest()
            matrices.push()
            matrices.translate(x.toFloat(), y.toFloat(), 0f)
            fill(matrices, 0, 0, DESIGN_AREA_WIDTH, DESIGN_AREA_HEIGHT, color(0x55d2fc))
            matrices.pop()

            matrices.push()
            applyFurnitureTransforms(matrices)
            drawCompass(matrices)

            // Draw each part in the scene
            for (part in menu.furnitureParts) {
                FurnitureDesignRenderer.drawPart(matrices, part, part === focusedPart, LightmapTextureManager.MAX_LIGHT_COORDINATE)
            }

            matrices.pop()
            RenderSystem.disableDepthTest()
            DrawableHelper.disableScissor()
        }

        override fun isMouseOver(mouseX: Double, mouseY: Double): Boolean =
            x <= mouseX && mouseX <= x + DESIGN_AREA_WIDTH && y <= mouseY && mouseY <= y + DESIGN_AREA_HEIGHT

        override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
            if (isMouseOver(mouseX, mouseY)) {
                // Calculate the transformation matrix from screen coordinates to furniture coordinates
                val matrices = MatrixStack()
                applyFurnitureTransforms(matrices)
                val matrixFromScreenToWorld = matrices.peek().positionMatrix.invert()

                // This line/ray goes from the cursor into the screen.
                val cursorPoint = Vector4f(mouseX.toFloat(), mouseY.toFloat(), 100f, 1f)
                    .mul(matrixFromScreenToWorld)
                val directionVector = Vector4f(0f, 0f, -1f, 0f).mul(matrixFromScreenToWorld)

                // Buffers
                val vec4Buffer = Vector4f()
                val vec3Buffer = Vector3f()

                // Sort the parts in order to use the part in front.
                val intersectingParts = ArrayList<Pair<FurniturePart, Float>>()

                // Find the intersecting part
                for (part in menu.furnitureParts) {
                    var distance = Float.MAX_VALUE // distance from cursor to intersection
                    var inside = false
                    part.forEachFace { x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4 ->
                        val normal = vec3Buffer.set(x2 - x1, y2 - y1, z2 - z1)
                            .cross((x3 - x1).toFloat(), (y3 - y1).toFloat(), (z3 - z1).toFloat())
                        val dot = normal.dot(directionVector.x, directionVector.y, directionVector.z)

                        // If normal is not perpendicular (dot != 0), the ray from the cursor intersects the plane the face lies on.
                        if (!MathHelper.approximatelyEquals(dot, 0f)) {
                            // Calculate the distance of the plane from the origin.
                            val d = -normal.dot(x4.toFloat(), y4.toFloat(), z4.toFloat())
                            val planeVector = Vector4f(normal.x, normal.y, normal.z, d)

                            // Intersection:
                            // i = p - (f * p)/(f * v) * v where f is the plane, and p + tv is the line
                            val intersection = Vector4f(cursorPoint).sub(
                                vec4Buffer.set(directionVector)
                                    .mul(planeVector.dot(cursorPoint) / planeVector.dot(directionVector))
                            )

                            val insideFace = Geometry.isPointInsideQuadrilateral(
                                intersection.x.toDouble(), intersection.y.toDouble(), intersection.z.toDouble(),
                                x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4
                            )

                            if (insideFace) {
                                distance = min(
                                    distance,
                                    vec3Buffer.set(intersection.x, intersection.y, intersection.z)
                                        .distance(cursorPoint.x, cursorPoint.y, cursorPoint.z)
                                )
                                inside = true
                            }
                        }
                    }

                    if (inside) {
                        intersectingParts += part to distance
                        continue
                    }
                }

                // Sort intersecting parts based on distance.
                intersectingParts.sortByDescending { (_, distance) -> distance }
                // The first part is the focused part.
                val newFocused = intersectingParts.firstOrNull()?.first
                focusedPart = newFocused

                if (newFocused != null) {
                    currentMaterial = newFocused.material
                }

                return true
            }

            return false
        }

        override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
            if (button == 0) {
                rotationY = (rotationY + (ROTATION_SPEED_AROUND_Y * deltaX).toFloat()) % MathHelper.TAU
                rotationXZ = MathHelper.clamp(
                    rotationXZ + (HORIZONTAL_ROTATION_SPEED * deltaY).toFloat(),
                    -MathHelper.HALF_PI,
                    MathHelper.HALF_PI
                )
                return true
            }

            return false
        }

        override fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean {
            zoom = MathHelper.clamp(zoom + amount.toFloat(), MIN_ZOOM, MAX_ZOOM)
            return true
        }

        override fun appendNarrations(builder: NarrationMessageBuilder) {
        }

        override fun getType(): Selectable.SelectionType =
            if (focused) {
                Selectable.SelectionType.FOCUSED
            } else if (hovered) {
                Selectable.SelectionType.HOVERED
            } else {
                Selectable.SelectionType.NONE
            }

        override fun changeFocus(lookForwards: Boolean): Boolean {
            focused = !focused
            return focused
        }
    }

    private inner class PageButton(x: Int, y: Int, private val forwards: Boolean) : PressableWidget(x, y, 12, 12, ScreenTexts.EMPTY) {
        override fun renderButton(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
            RenderSystem.setShaderTexture(0, WIDGETS)
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
            val u = if (forwards) 72 else 60
            val v = if (isHovered) 12 else 0
            drawTexture(matrices, x, y, u, v, width, height)
        }

        override fun appendClickableNarrations(builder: NarrationMessageBuilder) {
            appendDefaultNarrations(builder)
        }

        override fun onPress() {
            if (forwards) {
                materialTabs.flipBook.currentPageValue.showNextPage()
            } else {
                materialTabs.flipBook.currentPageValue.showPreviousPage()
            }
        }
    }
}
