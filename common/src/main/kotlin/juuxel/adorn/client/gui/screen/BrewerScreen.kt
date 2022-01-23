package juuxel.adorn.client.gui.screen

import com.mojang.blaze3d.systems.RenderSystem
import juuxel.adorn.AdornCommon
import juuxel.adorn.block.entity.BrewerBlockEntity
import juuxel.adorn.menu.BrewerMenu
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.minecraft.util.math.MathHelper

class BrewerScreen(menu: BrewerMenu, playerInventory: PlayerInventory, title: Text) : AdornMenuScreen<BrewerMenu>(menu, playerInventory, title) {
    override fun drawBackground(matrices: MatrixStack, delta: Float, mouseX: Int, mouseY: Int) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader)
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        RenderSystem.setShaderTexture(0, TEXTURE)
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight)

        val progress = handler.progress
        if (progress > 0) {
            val progressFract = progress.toFloat() / BrewerBlockEntity.MAX_PROGRESS.toFloat()
            drawTexture(matrices, x + 84, y + 24, 176, 0, 8, MathHelper.ceil(progressFract * 25))
        }
    }

    companion object {
        val TEXTURE = AdornCommon.id("textures/gui/brewer.png")
    }
}
