package juuxel.adorn.client.renderer

import juuxel.adorn.block.AbstractKitchenCounterBlock
import juuxel.adorn.block.entity.KitchenSinkBlockEntity
import juuxel.adorn.client.FluidRenderingBridge
import juuxel.adorn.util.logger
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.texture.MissingSprite
import net.minecraft.client.texture.Sprite
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3f

@Environment(EnvType.CLIENT)
abstract class KitchenSinkRenderer<T : KitchenSinkBlockEntity>(context: BlockEntityRendererFactory.Context) : BlockEntityRenderer<T> {
    override fun render(entity: T, tickDelta: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int, overlay: Int) {
        // Skip if there's nothing to render
        if (isEmpty(entity)) return

        val sprite = getFluidSprite(entity)
        val buffer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(sprite.atlas.id))
        val u0 = MathHelper.lerp(2 * PX, sprite.minU, sprite.maxU)
        val u1 = MathHelper.lerp(14 * PX, sprite.minU, sprite.maxU)
        val v0 = MathHelper.lerp(2 * PX, sprite.minV, sprite.maxV)
        val v1 = MathHelper.lerp(13 * PX, sprite.minV, sprite.maxV)

        matrices.push()
        // Rotate because the model depends on the facing property
        matrices.translate(0.5, 0.0, 0.5)
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(getRotation(entity.cachedState[AbstractKitchenCounterBlock.FACING])))
        matrices.translate(-0.5, 0.0, -0.5)

        // Move vertically to correct level
        val fluidLevel = getFluidLevel(entity) / LITRES_PER_BLOCK
        matrices.translate(0.0, MathHelper.lerp(fluidLevel, Y_START.toDouble(), Y_END.toDouble()), 0.0)

        // Draw the sprite
        fun y(x: Float, z: Float): Float {
            val time = (MinecraftClient.getInstance().player!!.age * MS_PER_TICK) % WAVE_PERIOD
            val t = time * MathHelper.TAU / WAVE_PERIOD
            return MathHelper.sin(t + x + z) * WAVE_HEIGHT / 2
        }

        val positionMatrix = matrices.peek().positionMatrix
        val normalMatrix = matrices.peek().normalMatrix
        val color = getFluidColor(entity)
        buffer.vertex(positionMatrix, X_START, y(X_START, Z_END), Z_END)
            .color(color).texture(u0, v0).overlay(overlay).light(light).normal(normalMatrix, 0f, 1f, 0f).next()
        buffer.vertex(positionMatrix, X_END, y(X_END, Z_END), Z_END)
            .color(color).texture(u0, v1).overlay(overlay).light(light).normal(normalMatrix, 0f, 1f, 0f).next()
        buffer.vertex(positionMatrix, X_END, y(X_END, Z_START), Z_START)
            .color(color).texture(u1, v1).overlay(overlay).light(light).normal(normalMatrix, 0f, 1f, 0f).next()
        buffer.vertex(positionMatrix, X_START, y(X_START, Z_START), Z_START)
            .color(color).texture(u1, v0).overlay(overlay).light(light).normal(normalMatrix, 0f, 1f, 0f).next()
        matrices.pop()
    }

    /** Gets the [entity]'s fluid's sprite. */
    private fun getFluidSprite(entity: T): Sprite {
        val sprite = FluidRenderingBridge.get().getStillSprite(entity.fluidReference)

        if (sprite == null) {
            LOGGER.error("Could not find sprite for fluid reference {} when rendering kitchen sink at {}", entity.fluidReference, entity.pos)
            return MinecraftClient.getInstance()
                .getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)
                .apply(MissingSprite.getMissingSpriteId())
        }

        return sprite
    }

    /** Gets the [entity]'s fluid's color. */
    private fun getFluidColor(entity: T): Int =
        FluidRenderingBridge.get().getColor(entity.fluidReference, entity.world, entity.pos)

    /** Gets the fluid level from the [entity] in litres. */
    protected abstract fun getFluidLevel(entity: T): Double

    /** Tests whether the [entity] has no fluid inside. */
    protected abstract fun isEmpty(entity: T): Boolean

    companion object {
        private val LOGGER = logger()
        private const val PX = 1 / 16f
        private const val X_START = 2 * PX
        private const val X_END = 13 * PX
        private const val Z_START = 2 * PX
        private const val Z_END = 14 * PX
        private const val Y_START = 7 * PX
        private const val Y_END = 15 * PX
        private const val LITRES_PER_BLOCK: Double = 1000.0

        // Wave period in ms
        private const val WAVE_PERIOD: Float = 12_000f
        private const val WAVE_HEIGHT: Float = PX
        private const val MS_PER_TICK: Float = 50f

        private fun getRotation(facing: Direction): Float =
            when (facing) {
                Direction.EAST -> 0f
                Direction.NORTH -> 90f
                Direction.WEST -> 180f
                Direction.SOUTH -> 270f
                // Vertical orientations
                else -> 0f
            }
    }
}
