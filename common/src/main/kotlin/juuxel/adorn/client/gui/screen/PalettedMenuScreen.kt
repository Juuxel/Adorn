package juuxel.adorn.client.gui.screen

import com.mojang.blaze3d.systems.RenderSystem
import juuxel.adorn.menu.ContainerBlockMenu
import juuxel.adorn.platform.PlatformBridges
import juuxel.adorn.util.Colors
import juuxel.adorn.util.getBlock
import net.minecraft.client.gui.DrawContext
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.menu.Menu
import net.minecraft.registry.Registries
import net.minecraft.text.Text
import net.minecraft.util.Identifier

abstract class PalettedMenuScreen<M>(menu: M, playerInventory: PlayerInventory, title: Text) :
    AdornMenuScreen<M>(menu, playerInventory, title)
    where M : Menu, M : ContainerBlockMenu {
    protected abstract val backgroundTexture: Identifier
    protected abstract val paletteId: Identifier
    private val blockId = Registries.BLOCK.getId(menu.context.getBlock())

    private fun getPalette() = PlatformBridges.resources.colorManager.getColors(paletteId)[blockId]

    override fun drawBackground(context: DrawContext, delta: Float, mouseX: Int, mouseY: Int) {
        val bg = getPalette().bg
        RenderSystem.setShaderColor(Colors.redOf(bg), Colors.greenOf(bg), Colors.blueOf(bg), 1.0f)
        context.drawTexture(backgroundTexture, x, y, 0, 0, backgroundWidth, backgroundHeight)
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
    }

    override fun drawForeground(context: DrawContext, mouseX: Int, mouseY: Int) {
        val fg = getPalette().fg
        context.drawText(textRenderer, title, titleX, titleY, fg, false)
        context.drawText(textRenderer, playerInventoryTitle, playerInventoryTitleX, playerInventoryTitleY, fg, false)
    }
}
