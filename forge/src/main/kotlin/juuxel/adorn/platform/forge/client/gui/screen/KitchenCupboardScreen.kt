package juuxel.adorn.platform.forge.client.gui.screen

import com.mojang.blaze3d.systems.RenderSystem
import juuxel.adorn.AdornCommon
import juuxel.adorn.platform.forge.menu.KitchenCupboardMenu
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text

class KitchenCupboardScreen(
    menu: KitchenCupboardMenu,
    playerInventory: PlayerInventory,
    title: Text
) : AdornMenuScreen<KitchenCupboardMenu>(menu, playerInventory, title) {
    override fun drawBackground(matrices: MatrixStack, delta: Float, mouseX: Int, mouseY: Int) {
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE)
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight)
    }

    companion object {
        private val BACKGROUND_TEXTURE = AdornCommon.id("textures/gui/kitchen_cupboard.png")
    }
}
