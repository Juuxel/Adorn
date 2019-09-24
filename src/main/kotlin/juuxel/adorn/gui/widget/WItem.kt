package juuxel.adorn.gui.widget

import com.mojang.blaze3d.platform.GlStateManager
import io.github.cottonmc.cotton.gui.widget.WWidget
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.GuiLighting
import net.minecraft.item.Item

class WItem(private val item: Item) : WWidget() {
    init {
        super.setSize(18, 18)
    }

    @Environment(EnvType.CLIENT)
    override fun paintBackground(x: Int, y: Int) {
        GlStateManager.pushMatrix()
        GlStateManager.enableDepthTest()
        GlStateManager.translatef(x.toFloat(), y.toFloat(), 0f)
        GlStateManager.scalef(1.2f, 1.2f, 1.0f)
        GuiLighting.enableForItems()
        val client = MinecraftClient.getInstance()
        val renderer = client.itemRenderer
        renderer.zOffset = 100f
        renderer.renderGuiItem(client.player, item.stackForRender, 0, 0)
        renderer.zOffset = 0f
        GuiLighting.disable()
        GlStateManager.popMatrix()
    }

    override fun canResize() = false

    override fun setSize(x: Int, y: Int) {}
}
