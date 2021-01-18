package juuxel.adorn.client.gui.screen

import juuxel.adorn.Adorn
import juuxel.adorn.menu.KitchenCupboardMenu
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text

class KitchenCupboardScreen(
    menu: KitchenCupboardMenu,
    playerInventory: PlayerInventory,
    title: Text
) : AdornMenuScreen<KitchenCupboardMenu>(menu, playerInventory, title) {
    override fun drawBackground(matrices: MatrixStack, delta: Float, mouseX: Int, mouseY: Int) {
        client!!.textureManager.bindTexture(BACKGROUND_TEXTURE)
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight)
    }

    companion object {
        private val BACKGROUND_TEXTURE = Adorn.id("textures/gui/kitchen_cupboard.png")
    }
}
