package juuxel.adorn.client.gui.screen

import com.mojang.blaze3d.systems.RenderSystem
import juuxel.adorn.AdornCommon
import juuxel.adorn.item.TradingStationUpgradeItem
import juuxel.adorn.menu.TradingStationMenu
import juuxel.adorn.util.Colors
import net.minecraft.client.gui.AbstractParentElement
import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.ParentElement
import net.minecraft.client.gui.Selectable
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import kotlin.properties.Delegates

class TradingStationScreen(
    menu: TradingStationMenu,
    playerInventory: PlayerInventory,
    title: Text
) : AdornMenuScreen<TradingStationMenu>(menu, playerInventory, title) {
    init {
        backgroundHeight = 186
        playerInventoryTitleY = backgroundHeight - 94 // copied from MenuScreen.<init>
    }

    override fun isClickOutsideBounds(mouseX: Double, mouseY: Double, left: Int, top: Int, button: Int): Boolean {
        val panelLeft = left + backgroundWidth + 4
        val panelRight = panelLeft + SIDE_PANEL_WIDTH

        if (super.isClickOutsideBounds(mouseX, mouseY, left, top, button)) {
            for (index in 0 until TradingStationMenu.UPGRADE_COUNT) {
                val item = menu.slots[50 + index].stack.item

                if (item is TradingStationUpgradeItem && top <= mouseY && mouseY <= (top + item.type.panelHeight) &&
                    panelLeft <= mouseX && mouseX <= panelRight) {
                    return false
                }
            }

            return true
        }

        return false
    }

    override fun drawBackground(matrices: MatrixStack, delta: Float, mouseX: Int, mouseY: Int) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader)
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE)
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight)
    }

    override fun drawForeground(matrices: MatrixStack, mouseX: Int, mouseY: Int) {
        textRenderer.draw(matrices, title, titleX.toFloat(), titleY.toFloat(), Colors.WHITE)
        textRenderer.draw(matrices, playerInventoryTitle, playerInventoryTitleX.toFloat(), playerInventoryTitleY.toFloat(), Colors.WHITE)
        textRenderer.draw(matrices, SELLING_LABEL, 17f + 8f - textRenderer.getWidth(SELLING_LABEL) / 2, 25f, Colors.WHITE)
        textRenderer.draw(matrices, PRICE_LABEL, 17f + 8f - textRenderer.getWidth(PRICE_LABEL) / 2, 61f, Colors.WHITE)
        textRenderer.draw(matrices, UPGRADES_LABEL, 143f + 8f - textRenderer.getWidth(UPGRADES_LABEL) / 2, 25f, Colors.WHITE)
    }

    companion object {
        private val BACKGROUND_TEXTURE = AdornCommon.id("textures/gui/trading_station.png")
        private val SELLING_LABEL: Text = TranslatableText("block.adorn.trading_station.selling")
        private val PRICE_LABEL: Text = TranslatableText("block.adorn.trading_station.price")
        private val UPGRADES_LABEL: Text = TranslatableText("block.adorn.trading_station.upgrades")
        private const val SIDE_PANEL_WIDTH: Int = 2 * 7 + 3 * 9
    }

    private open inner class SidePanel(x: Int, y: Int) : AbstractParentElement(), Drawable, Selectable {
        var x: Int by Delegates.observable(x) { _, _, x -> refreshPosition(x, y) }
        var y: Int by Delegates.observable(y) { _, _, y -> refreshPosition(x, y) }

        override fun children(): List<Element> = listOf()

        protected open fun refreshPosition(x: Int, y: Int) {}

        override fun render(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
            TODO("Not yet implemented")
        }

        override fun appendNarrations(builder: NarrationMessageBuilder?) {
            TODO("Not yet implemented")
        }

        override fun getType(): Selectable.SelectionType {
            TODO("Not yet implemented")
        }
    }

    private inner class IoSidePanel(x: Int, y: Int) : SidePanel(x, y) {
        override fun children(): List<Element> {
            return super.children()
        }
    }

    private inner class IoButton(var x: Int, var y: Int)
}
