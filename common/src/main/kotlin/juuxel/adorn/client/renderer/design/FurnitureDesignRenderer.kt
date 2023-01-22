package juuxel.adorn.client.renderer.design

import com.mojang.blaze3d.systems.RenderSystem
import juuxel.adorn.client.resources.BlockVariantTextureLoader
import juuxel.adorn.design.FurniturePart
import juuxel.adorn.util.Cache
import juuxel.adorn.util.Colors
import juuxel.adorn.util.color
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.BufferRenderer
import net.minecraft.client.render.DiffuseLighting
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.texture.MissingSprite
import net.minecraft.client.texture.Sprite
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import org.joml.Matrix3f
import org.joml.Matrix4f
import kotlin.math.abs
import kotlin.math.sin

object FurnitureDesignRenderer {
    private val SELECTED_HIGHLIGHT_COLOR = color(0xa4e0fc)
    private const val HIGHLIGHT_PULSE_PERIOD_MS = 3000.0

    private val client: MinecraftClient = MinecraftClient.getInstance()
    private val spriteCache: Cache<Identifier, Sprite> = Cache.soft()

    fun drawPart(matrices: MatrixStack, part: FurniturePart, highlighted: Boolean, light: Int) {
        val texture = BlockVariantTextureLoader.get(part.material.id)?.mainTexture ?: MissingSprite.getMissingSpriteId()
        val sprite = spriteCache.getOrPut(texture) {
            val atlas = client.getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)
            atlas.apply(texture)
        }

        // TODO: Rotation
        RenderSystem.enableBlend()
        DiffuseLighting.enableGuiDepthLighting()
        RenderSystem.setShader(GameRenderer::getPositionColorTexLightmapProgram)
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        RenderSystem.setShaderTexture(0, sprite.atlasId)

        val matrixEntry = matrices.peek()
        val position = matrixEntry.positionMatrix
        val normal = matrixEntry.normalMatrix
        part.forEachFace { x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4 ->
            drawFace(position, normal, x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, sprite, highlighted, light)
        }
        DiffuseLighting.disableGuiDepthLighting()
        RenderSystem.disableBlend()
    }

    private fun drawFace(
        positionMatrix: Matrix4f,
        normalMatrix: Matrix3f,
        x1: Double, y1: Double, z1: Double,
        x2: Double, y2: Double, z2: Double,
        x3: Double, y3: Double, z3: Double,
        x4: Double, y4: Double, z4: Double,
        sprite: Sprite, highlighted: Boolean,
        light: Int
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
        val rawMinU: Float
        val rawMinV: Float
        val rawMaxU: Float
        val rawMaxV: Float
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
                rawMinV = MathHelper.map(maxY, 16.0, 0.0, 0.0, 1.0).toFloat()
                rawMaxV = MathHelper.map(minY, 16.0, 0.0, 0.0, 1.0).toFloat()

                // along z-axis
                if (normalZ < 0) {
                    rawMinU = MathHelper.map(maxX, 16.0, 0.0, 0.0, 1.0).toFloat()
                    rawMaxU = MathHelper.map(minX, 16.0, 0.0, 0.0, 1.0).toFloat()
                } else {
                    rawMinU = MathHelper.map(minX, 0.0, 16.0, 0.0, 1.0).toFloat()
                    rawMaxU = MathHelper.map(maxX, 0.0, 16.0, 0.0, 1.0).toFloat()
                }
            } else {
                // along y-axis
                rawMinU = MathHelper.map(minX, 0.0, 16.0, 0.0, 1.0).toFloat()
                rawMaxU = MathHelper.map(maxX, 0.0, 16.0, 0.0, 1.0).toFloat()

                if (normalY < 0) {
                    rawMinV = MathHelper.map(maxZ, 16.0, 0.0, 0.0, 1.0).toFloat()
                    rawMaxV = MathHelper.map(minZ, 16.0, 0.0, 0.0, 1.0).toFloat()
                } else {
                    rawMinV = MathHelper.map(maxZ, 0.0, 16.0, 0.0, 1.0).toFloat()
                    rawMaxV = MathHelper.map(minZ, 0.0, 16.0, 0.0, 1.0).toFloat()
                }
            }
        } else {
            // along x-axis
            rawMinV = MathHelper.map(maxY, 16.0, 0.0, 0.0, 1.0).toFloat()
            rawMaxV = MathHelper.map(minY, 16.0, 0.0, 0.0, 1.0).toFloat()

            if (normalX < 0) {
                rawMinU = MathHelper.map(maxZ, 16.0, 0.0, 0.0, 1.0).toFloat()
                rawMaxU = MathHelper.map(minZ, 16.0, 0.0, 0.0, 1.0).toFloat()
            } else {
                rawMinU = MathHelper.map(minZ, 0.0, 16.0, 0.0, 1.0).toFloat()
                rawMaxU = MathHelper.map(maxZ, 0.0, 16.0, 0.0, 1.0).toFloat()
            }
        }
        val minU = MathHelper.lerp(rawMinU, sprite.minU, sprite.maxU)
        val maxU = MathHelper.lerp(rawMaxU, sprite.minU, sprite.maxU)
        val minV = MathHelper.lerp(rawMinV, sprite.minV, sprite.maxV)
        val maxV = MathHelper.lerp(rawMaxV, sprite.minV, sprite.maxV)

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
            .texture(maxU, maxV).light(light)
            .normal(normalMatrix, normalX, normalY, normalZ).next()
        buffer.vertex(positionMatrix, x2.toFloat(), y2.toFloat(), z2.toFloat()).color(color)
            .texture(maxU, minV).light(light)
            .normal(normalMatrix, normalX, normalY, normalZ).next()
        buffer.vertex(positionMatrix, x3.toFloat(), y3.toFloat(), z3.toFloat()).color(color)
            .texture(minU, minV).light(light)
            .normal(normalMatrix, normalX, normalY, normalZ).next()
        buffer.vertex(positionMatrix, x4.toFloat(), y4.toFloat(), z4.toFloat()).color(color)
            .texture(minU, maxV).light(light)
            .normal(normalMatrix, normalX, normalY, normalZ).next()
        BufferRenderer.drawWithGlobalProgram(buffer.end())
    }
}
