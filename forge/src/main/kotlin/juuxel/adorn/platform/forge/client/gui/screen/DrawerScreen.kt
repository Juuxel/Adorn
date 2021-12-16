package juuxel.adorn.platform.forge.client.gui.screen

import com.mojang.blaze3d.systems.RenderSystem
import juuxel.adorn.AdornCommon
import juuxel.adorn.platform.PlatformBridges
import juuxel.adorn.platform.forge.menu.DrawerMenu
import juuxel.adorn.util.Colors
import juuxel.adorn.util.getBlock
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.minecraft.util.registry.Registry

class DrawerScreen(
    menu: DrawerMenu,
    playerInventory: PlayerInventory,
    title: Text
) : AdornMenuScreen<DrawerMenu>(menu, playerInventory, title) {
    private val blockId = Registry.BLOCK.getId(menu.context.getBlock())
    private fun getPalette() = PlatformBridges.resources.colorManager.getColors(PALETTE_ID)[blockId]

    override fun drawBackground(matrices: MatrixStack, delta: Float, mouseX: Int, mouseY: Int) {
        val bg = getPalette().bg
        RenderSystem.setShaderColor(Colors.redOf(bg), Colors.greenOf(bg), Colors.blueOf(bg), 1.0f)
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE)
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight)
    }

    override fun drawForeground(matrices: MatrixStack, mouseX: Int, mouseY: Int) {
        val fg = getPalette().fg
        textRenderer.draw(matrices, title, titleX.toFloat(), titleY.toFloat(), fg)
        textRenderer.draw(matrices, playerInventoryTitle, playerInventoryTitleX.toFloat(), playerInventoryTitleY.toFloat(), fg)
    }

    companion object {
        private val BACKGROUND_TEXTURE = AdornCommon.id("textures/gui/drawer.png")
        private val PALETTE_ID = AdornCommon.id("drawer")
    }
}
