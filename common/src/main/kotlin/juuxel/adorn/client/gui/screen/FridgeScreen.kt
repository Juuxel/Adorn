package juuxel.adorn.client.gui.screen

import com.mojang.blaze3d.systems.RenderSystem
import juuxel.adorn.menu.FridgeMenu
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class FridgeScreen(menu: FridgeMenu, playerInventory: PlayerInventory, title: Text) : AdornMenuScreen<FridgeMenu>(menu, playerInventory, title) {
    override fun drawBackground(matrices: MatrixStack, delta: Float, mouseX: Int, mouseY: Int) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram)
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE)
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight)
    }

    companion object {
        // TODO: Fridge texture
        private val BACKGROUND_TEXTURE = Identifier("minecraft", "textures/gui/container/shulker_box.png")
    }
}
