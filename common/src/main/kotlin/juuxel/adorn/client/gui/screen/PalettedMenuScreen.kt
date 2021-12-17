package juuxel.adorn.client.gui.screen

import com.mojang.blaze3d.systems.RenderSystem
import juuxel.adorn.menu.ContainerBlockMenu
import juuxel.adorn.platform.PlatformBridges
import juuxel.adorn.util.Colors
import juuxel.adorn.util.getBlock
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

abstract class PalettedMenuScreen<M>(menu: M, playerInventory: PlayerInventory, title: Text) :
    AdornMenuScreen<M>(menu, playerInventory, title)
    where M : ScreenHandler, M : ContainerBlockMenu {
    protected abstract val backgroundTexture: Identifier
    protected abstract val paletteId: Identifier
    private val blockId = Registry.BLOCK.getId(menu.context.getBlock())

    private fun getPalette() = PlatformBridges.resources.colorManager.getColors(paletteId)[blockId]

    override fun drawBackground(matrices: MatrixStack, delta: Float, mouseX: Int, mouseY: Int) {
        val bg = getPalette().bg
        RenderSystem.setShaderColor(Colors.redOf(bg), Colors.greenOf(bg), Colors.blueOf(bg), 1.0f)
        RenderSystem.setShaderTexture(0, backgroundTexture)
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight)
    }

    override fun drawForeground(matrices: MatrixStack, mouseX: Int, mouseY: Int) {
        val fg = getPalette().fg
        textRenderer.draw(matrices, title, titleX.toFloat(), titleY.toFloat(), fg)
        textRenderer.draw(matrices, playerInventoryTitle, playerInventoryTitleX.toFloat(), playerInventoryTitleY.toFloat(), fg)
    }
}
