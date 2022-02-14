package juuxel.adorn.client.gui.screen

import com.mojang.blaze3d.systems.RenderSystem
import juuxel.adorn.AdornCommon
import juuxel.adorn.block.entity.BrewerBlockEntity
import juuxel.adorn.menu.BrewerMenu
import juuxel.adorn.menu.FluidVolume
import juuxel.adorn.platform.FluidRenderingBridge
import juuxel.adorn.util.color
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.ingame.MenuProvider
import net.minecraft.client.render.BufferRenderer
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.texture.Sprite
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.minecraft.util.math.MathHelper
import org.slf4j.LoggerFactory

class BrewerScreen(menu: BrewerMenu, playerInventory: PlayerInventory, title: Text) : AdornMenuScreen<BrewerMenu>(menu, playerInventory, title) {
    override fun drawBackground(matrices: MatrixStack, delta: Float, mouseX: Int, mouseY: Int) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader)
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        RenderSystem.setShaderTexture(0, TEXTURE)
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight)
        drawFluid(matrices)
        drawTexture(matrices, x + 145, y + 17, 176, 25, 16, 51)

        val progress = menu.progress
        if (progress > 0) {
            val progressFract = progress.toFloat() / BrewerBlockEntity.MAX_PROGRESS.toFloat()
            drawTexture(matrices, x + 84, y + 24, 176, 0, 8, MathHelper.ceil(progressFract * 25))
        }
    }

    private fun drawFluid(matrices: MatrixStack) {
        if (menu.fluid.isEmpty) return

        val bridge = FluidRenderingBridge.get()
        val sprite = bridge.getStillSprite(menu.fluid) ?: run {
            LOGGER.warn("Could not find sprite for {} in brewer screen", menu.fluid)
            return
        }

        val color = color(bridge.getColor(menu.fluid))
        val height = FLUID_AREA_HEIGHT * (menu.fluid.amount / (4 * bridge.bucketVolume).toFloat())
        var fluidY = 0

        fun transformY(areaHeight: Float): Float =
            if (bridge.fillsFromTop(menu.fluid)) (17 + fluidY).toFloat()
            else 17 + FLUID_AREA_HEIGHT - fluidY - areaHeight

        for (i in 1..MathHelper.floor(height / 16)) {
            drawSprite(matrices, x + 145, y + transformY(16f), 16f, 16f, 0f, 0f, 1f, 1f, sprite, color)
            fluidY += 16
        }

        val leftover = height % 16
        drawSprite(matrices, x + 145, y + transformY(leftover), 16f, leftover, 0f, 0f, 1f, leftover / 16f, sprite, color)
    }

    private fun drawSprite(matrices: MatrixStack, x: Int, y: Float, width: Float, height: Float, u0: Float, v0: Float, u1: Float, v1: Float, sprite: Sprite, color: Int) {
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader)
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        RenderSystem.setShaderTexture(0, SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)
        val buffer = Tessellator.getInstance().buffer
        val positionMatrix = matrices.peek().positionMatrix
        val au0 = MathHelper.lerp(u0, sprite.minU, sprite.maxU)
        val au1 = MathHelper.lerp(u1, sprite.minU, sprite.maxU)
        val av0 = MathHelper.lerp(v0, sprite.minV, sprite.maxV)
        val av1 = MathHelper.lerp(v1, sprite.minV, sprite.maxV)
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR)
        buffer.vertex(positionMatrix, x.toFloat(), y, 0f).texture(au0, av0).color(color).next()
        buffer.vertex(positionMatrix, x.toFloat() + width, y, 0f).texture(au1, av0).color(color).next()
        buffer.vertex(positionMatrix, x.toFloat() + width, y + height, 0f).texture(au1, av1).color(color).next()
        buffer.vertex(positionMatrix, x.toFloat(), y + height, 0f).texture(au0, av1).color(color).next()
        buffer.end()
        BufferRenderer.draw(buffer)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(BrewerScreen::class.java)
        val TEXTURE = AdornCommon.id("textures/gui/brewer.png")
        private const val FLUID_AREA_HEIGHT: Int = 51

        fun setFluidFromPacket(client: MinecraftClient, syncId: Int, fluid: FluidVolume) {
            val screen = client.currentScreen
            if (screen is MenuProvider<*>) {
                val menu = screen.menu
                if (menu.syncId == syncId && menu is BrewerMenu) {
                    menu.fluid = fluid
                }
            }
        }
    }
}
