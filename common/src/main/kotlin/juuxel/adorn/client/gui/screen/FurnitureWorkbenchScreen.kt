package juuxel.adorn.client.gui.screen

import com.mojang.blaze3d.systems.RenderSystem
import juuxel.adorn.AdornCommon
import juuxel.adorn.client.gui.widget.NoOpSelectable
import juuxel.adorn.design.FurniturePart
import juuxel.adorn.menu.FurnitureWorkbenchMenu
import juuxel.adorn.util.Colors
import juuxel.adorn.util.Geometry
import juuxel.adorn.util.color
import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.render.BufferRenderer
import net.minecraft.client.render.DiffuseLighting
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.LightmapTextureManager
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.RotationAxis
import org.joml.Matrix3f
import org.joml.Matrix4f
import org.joml.Vector3d
import org.joml.Vector3f
import org.joml.Vector4f
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

class FurnitureWorkbenchScreen(menu: FurnitureWorkbenchMenu, playerInventory: PlayerInventory, title: Text) :
    AdornMenuScreen<FurnitureWorkbenchMenu>(menu, playerInventory, title) {
    private val furnitureParts: MutableList<FurniturePart> = ArrayList()

    init {
        furnitureParts += FurniturePart(Vector3d(8.0, 8.0, 8.0), 4, 4, 4)
    }

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
            furnitureParts += FurniturePart(
                Vector3d(x.toDouble() + width / 2, y.toDouble() + height / 2, z.toDouble() + depth / 2),
                width / 2, height / 2, depth / 2
            )
        }
            .position(5, y)
            .build()
        addDrawableChild(button)
    }

    override fun drawBackground(matrices: MatrixStack, delta: Float, mouseX: Int, mouseY: Int) {
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

        val px = 1 / 64f
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

    private fun drawPart(matrices: MatrixStack, part: FurniturePart, highlighted: Boolean) {
        // TODO: Rotation
        DiffuseLighting.enableGuiDepthLighting()
        RenderSystem.setShader(GameRenderer::getPositionColorTexLightmapProgram)
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        RenderSystem.setShaderTexture(0, TEST_TEXTURE)

        val matrixEntry = matrices.peek()
        val position = matrixEntry.positionMatrix
        val normal = matrixEntry.normalMatrix
        part.forEachFace { x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4 ->
            drawFace(position, normal, x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, highlighted)
        }
        DiffuseLighting.disableGuiDepthLighting()
    }

    private fun drawFace(
        positionMatrix: Matrix4f,
        normalMatrix: Matrix3f,
        x1: Double, y1: Double, z1: Double,
        x2: Double, y2: Double, z2: Double,
        x3: Double, y3: Double, z3: Double,
        x4: Double, y4: Double, z4: Double,
        highlighted: Boolean
    ) {
        val v1 = x2 - x1
        val v2 = y2 - y1
        val v3 = z2 - z1
        val w1 = x4 - x1
        val w2 = y4 - y1
        val w3 = z4 - z1
        var normalX = (v2 * w3 - v3 * w2).toFloat()
        var normalY = (v3 * w1 - v1 * w3).toFloat()
        var normalZ = (v1 * w2 - v2 * w1).toFloat()
        val scale = MathHelper.fastInverseSqrt(normalX * normalX + normalY * normalY + normalZ * normalZ)
        normalX *= scale
        normalY *= scale
        normalZ *= scale

        // Calculate UVs
        val minU: Float
        val minV: Float
        val maxU: Float
        val maxV: Float
        val nxZero = abs(normalX) < 0.1f
        val nyZero = abs(normalY) < 0.1f
        val minX = minOf(x1, x2, x3, x4)
        val minY = minOf(y1, y2, y3, y4)
        val minZ = minOf(z1, z2, z3, z4)
        val maxX = maxOf(x1, x2, x3, x4)
        val maxY = maxOf(y1, y2, y3, y4)
        val maxZ = maxOf(z1, z2, z3, z4)

        if (nxZero) {
            if (nyZero) {
                minV = MathHelper.map(maxY, 16.0, 0.0, 0.0, 1.0).toFloat()
                maxV = MathHelper.map(minY, 16.0, 0.0, 0.0, 1.0).toFloat()

                // along z-axis
                if (normalZ < 0) {
                    minU = MathHelper.map(maxX, 16.0, 0.0, 0.0, 1.0).toFloat()
                    maxU = MathHelper.map(minX, 16.0, 0.0, 0.0, 1.0).toFloat()
                } else {
                    minU = MathHelper.map(minX, 0.0, 16.0, 0.0, 1.0).toFloat()
                    maxU = MathHelper.map(maxX, 0.0, 16.0, 0.0, 1.0).toFloat()
                }
            } else {
                // along y-axis
                minU = MathHelper.map(minX, 0.0, 16.0, 0.0, 1.0).toFloat()
                maxU = MathHelper.map(maxX, 0.0, 16.0, 0.0, 1.0).toFloat()

                if (normalY < 0) {
                    minV = MathHelper.map(maxZ, 16.0, 0.0, 0.0, 1.0).toFloat()
                    maxV = MathHelper.map(minZ, 16.0, 0.0, 0.0, 1.0).toFloat()
                } else {
                    minV = MathHelper.map(maxZ, 0.0, 16.0, 0.0, 1.0).toFloat()
                    maxV = MathHelper.map(minZ, 0.0, 16.0, 0.0, 1.0).toFloat()
                }
            }
        } else {
            // along x-axis
            minV = MathHelper.map(maxY, 16.0, 0.0, 0.0, 1.0).toFloat()
            maxV = MathHelper.map(minY, 16.0, 0.0, 0.0, 1.0).toFloat()

            if (normalX < 0) {
                minU = MathHelper.map(maxZ, 16.0, 0.0, 0.0, 1.0).toFloat()
                maxU = MathHelper.map(minZ, 16.0, 0.0, 0.0, 1.0).toFloat()
            } else {
                minU = MathHelper.map(minZ, 0.0, 16.0, 0.0, 1.0).toFloat()
                maxU = MathHelper.map(maxZ, 0.0, 16.0, 0.0, 1.0).toFloat()
            }
        }

        val color = if (highlighted) {
            val time = System.currentTimeMillis() % HIGHLIGHT_PULSE_PERIOD_MS / HIGHLIGHT_PULSE_PERIOD_MS * MathHelper.TAU
            val delta = MathHelper.map(sin(time), -1.0, 1.0, 0.0, 1.0)
            Colors.lerp(Colors.WHITE, SELECTED_HIGHLIGHT_COLOR, delta.toFloat())
        } else {
            Colors.WHITE
        }

        val buffer = Tessellator.getInstance().buffer
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL)
        buffer.vertex(positionMatrix, x1.toFloat(), y1.toFloat(), z1.toFloat()).color(color)
            .texture(maxU, maxV).light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
            .normal(normalMatrix, normalX, normalY, normalZ).next()
        buffer.vertex(positionMatrix, x2.toFloat(), y2.toFloat(), z2.toFloat()).color(color)
            .texture(maxU, minV).light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
            .normal(normalMatrix, normalX, normalY, normalZ).next()
        buffer.vertex(positionMatrix, x3.toFloat(), y3.toFloat(), z3.toFloat()).color(color)
            .texture(minU, minV).light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
            .normal(normalMatrix, normalX, normalY, normalZ).next()
        buffer.vertex(positionMatrix, x4.toFloat(), y4.toFloat(), z4.toFloat()).color(color)
            .texture(minU, maxV).light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
            .normal(normalMatrix, normalX, normalY, normalZ).next()
        BufferRenderer.drawWithGlobalProgram(buffer.end())
    }

    companion object {
        private const val DESIGN_AREA_WIDTH = 8 * 18
        private const val DESIGN_AREA_HEIGHT = 5 * 18
        // Rotation speeds in radians per pixel
        private const val ROTATION_SPEED_AROUND_Y = 0.03
        private const val ROTATION_SPEED_AROUND_X = 0.03
        private const val MIN_ZOOM = 1f
        private const val MAX_ZOOM = 5f
        private val WIDGETS = AdornCommon.id("textures/gui/furniture_workbench_widgets.png")
        private val TEST_TEXTURE = AdornCommon.id("textures/block/crate.png")
        private val SELECTED_HIGHLIGHT_COLOR = color(0x55d2fc)
        private const val HIGHLIGHT_PULSE_PERIOD_MS = 3000.0
    }

    private inner class DesignView(private val x: Int, private val y: Int) : NoOpSelectable(), Drawable, Element {
        private var zoom = 3f
        private var rotationY = MathHelper.PI
        private var rotationX = -MathHelper.PI * 0.1f
        private var focusedPart: FurniturePart? = null

        private fun applyFurnitureTransforms(matrices: MatrixStack) {
            matrices.translate(x + 0.5f * DESIGN_AREA_WIDTH, y + 0.5f * DESIGN_AREA_HEIGHT, 100f)
            matrices.scale(zoom, -zoom, zoom)
            matrices.translate(0f, 0f, 8f)
            matrices.multiply(RotationAxis.POSITIVE_Y.rotation(rotationY))
            matrices.multiply(RotationAxis.POSITIVE_X.rotation(rotationX))
            matrices.translate(-8f, -8f, -8f)
        }

        override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
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
            for (part in furnitureParts) {
                drawPart(matrices, part, part == focusedPart)
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
                for (part in furnitureParts) {
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
                focusedPart = intersectingParts.firstOrNull()?.first

                return true
            }

            return false
        }

        override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
            if (button == 0) {
                rotationY = (rotationY + (ROTATION_SPEED_AROUND_Y * deltaX).toFloat()) % MathHelper.TAU
                rotationX = MathHelper.clamp(
                    rotationX - (ROTATION_SPEED_AROUND_X * deltaY).toFloat(),
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
    }
}
