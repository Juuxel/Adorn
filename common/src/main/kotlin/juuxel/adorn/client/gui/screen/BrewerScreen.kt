package juuxel.adorn.client.gui.screen

import com.mojang.blaze3d.systems.RenderSystem
import juuxel.adorn.AdornCommon
import juuxel.adorn.block.entity.BrewerBlockEntity
import juuxel.adorn.client.FluidRenderingBridge
import juuxel.adorn.fluid.FluidReference
import juuxel.adorn.fluid.FluidVolume
import juuxel.adorn.menu.BrewerMenu
import juuxel.adorn.util.color
import juuxel.adorn.util.logger
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.ingame.MenuProvider
import net.minecraft.client.item.TooltipContext
import net.minecraft.client.render.BufferRenderer
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.texture.Sprite
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.minecraft.util.math.MathHelper

class BrewerScreen(menu: BrewerMenu, playerInventory: PlayerInventory, title: Text) : AdornMenuScreen<BrewerMenu>(menu, playerInventory, title) {
    override fun drawBackground(matrices: MatrixStack, delta: Float, mouseX: Int, mouseY: Int) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram)
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        RenderSystem.setShaderTexture(0, TEXTURE)
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight)
        drawFluid(matrices, x + 145, y + 17, menu.fluid)
        RenderSystem.setShader(GameRenderer::getPositionTexProgram)
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        RenderSystem.setShaderTexture(0, TEXTURE)
        drawTexture(matrices, x + 145, y + 21, 176, 25, 16, 51)

        val progress = menu.progress
        if (progress > 0) {
            val progressFract = progress.toFloat() / BrewerBlockEntity.MAX_PROGRESS.toFloat()
            drawTexture(matrices, x + 84, y + 24, 176, 0, 8, MathHelper.ceil(progressFract * 25))
        }
    }

    override fun drawMouseoverTooltip(matrices: MatrixStack, x: Int, y: Int) {
        super.drawMouseoverTooltip(matrices, x, y)
        val x2 = x - this.x
        val y2 = y - this.y
        if (x2 in 145 until (145 + 16) && y2 in 17 until (17 + FLUID_AREA_HEIGHT)) {
            renderTooltip(matrices, getFluidTooltip(menu.fluid), x, y)
        }
    }

    private fun getFluidTooltip(fluid: FluidReference): List<Text> =
        FluidRenderingBridge.get().getTooltip(
            fluid,
            if (client!!.options.advancedItemTooltips) {
                TooltipContext.ADVANCED
            } else {
                TooltipContext.BASIC
            },
            maxAmountInLitres = BrewerBlockEntity.FLUID_CAPACITY_IN_BUCKETS * 1000
        )

    companion object {
        private val LOGGER = logger()
        val TEXTURE = AdornCommon.id("textures/gui/brewer.png")
        const val FLUID_AREA_HEIGHT: Int = 59

        fun setFluidFromPacket(client: MinecraftClient, syncId: Int, fluid: FluidVolume) {
            val screen = client.currentScreen
            if (screen is MenuProvider<*>) {
                val menu = screen.menu
                if (menu.syncId == syncId && menu is BrewerMenu) {
                    menu.fluid = fluid
                }
            }
        }

        private fun drawSprite(
            matrices: MatrixStack,
            x: Int, y: Float,
            width: Float, height: Float,
            u0: Float, v0: Float,
            u1: Float, v1: Float,
            sprite: Sprite, color: Int
        ) {
            RenderSystem.enableBlend()
            RenderSystem.setShader(GameRenderer::getPositionColorTexProgram)
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
            RenderSystem.setShaderTexture(0, sprite.atlasId)
            val buffer = Tessellator.getInstance().buffer
            val positionMatrix = matrices.peek().positionMatrix
            val au0 = MathHelper.lerp(u0, sprite.minU, sprite.maxU)
            val au1 = MathHelper.lerp(u1, sprite.minU, sprite.maxU)
            val av0 = MathHelper.lerp(v0, sprite.minV, sprite.maxV)
            val av1 = MathHelper.lerp(v1, sprite.minV, sprite.maxV)
            buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE)
            buffer.vertex(positionMatrix, x.toFloat(), y + height, 0f).color(color).texture(au0, av1).next()
            buffer.vertex(positionMatrix, x + width, y + height, 0f).color(color).texture(au1, av1).next()
            buffer.vertex(positionMatrix, x + width, y, 0f).color(color).texture(au1, av0).next()
            buffer.vertex(positionMatrix, x.toFloat(), y, 0f).color(color).texture(au0, av0).next()
            BufferRenderer.drawWithGlobalProgram(buffer.end())
            RenderSystem.disableBlend()
        }

        fun drawFluid(matrices: MatrixStack, x: Int, y: Int, fluid: FluidReference) {
            if (fluid.isEmpty) return

            val bridge = FluidRenderingBridge.get()
            val sprite = bridge.getStillSprite(fluid) ?: run {
                LOGGER.warn("Could not find sprite for {} in brewer screen", fluid)
                return
            }

            val color = color(bridge.getColor(fluid))
            val height = FLUID_AREA_HEIGHT * (fluid.amount / (BrewerBlockEntity.FLUID_CAPACITY_IN_BUCKETS * fluid.unit.bucketVolume).toFloat())
            var fluidY = 0

            fun transformY(areaHeight: Float): Float =
                if (bridge.fillsFromTop(fluid)) {
                    fluidY.toFloat()
                } else {
                    FLUID_AREA_HEIGHT - fluidY - areaHeight
                }

            for (i in 0 until MathHelper.floor(height / 16)) {
                drawSprite(matrices, x, y + transformY(16f), 16f, 16f, 0f, 0f, 1f, 1f, sprite, color)
                fluidY += 16
            }

            val leftover = height % 16
            drawSprite(matrices, x, y + transformY(leftover), 16f, leftover, 0f, 0f, 1f, leftover / 16f, sprite, color)
        }
    }
}
