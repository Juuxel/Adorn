package juuxel.adorn.gui.widget

import com.mojang.blaze3d.platform.GlStateManager
import io.github.cottonmc.cotton.gui.widget.WWidget
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.GuiLighting
import net.minecraft.item.Item
import net.minecraft.util.Tickable

class WItem(private val items: List<Item>) : WWidget(), Tickable {
    private var countdown = COUNTDOWN
    private var index = 0

    init {
        require(items.isNotEmpty()) { "There must be at least one item in an item widget" }
        super.setSize(18, 18)
    }

    override fun tick() {
        if (--countdown <= 0) {
            countdown = COUNTDOWN
            index = (index + 1) % items.size
        }
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
        renderer.renderGuiItem(client.player, items[index].stackForRender, 0, 0)
        renderer.zOffset = 0f
        GuiLighting.disable()
        GlStateManager.popMatrix()
    }

    override fun canResize() = false

    override fun setSize(x: Int, y: Int) {}

    companion object {
        private const val COUNTDOWN = 25
    }
}
