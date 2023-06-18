package juuxel.adorn.client.gui.screen

import juuxel.adorn.AdornCommon
import juuxel.adorn.client.ClientNetworkBridge
import juuxel.adorn.menu.TradingStationMenu
import juuxel.adorn.util.Colors
import juuxel.adorn.util.logger
import net.minecraft.client.gui.DrawContext
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.menu.Slot
import net.minecraft.text.Text

class TradingStationScreen(
    menu: TradingStationMenu,
    playerInventory: PlayerInventory,
    title: Text
) : AdornMenuScreen<TradingStationMenu>(menu, playerInventory, title) {
    init {
        backgroundHeight = 186
        playerInventoryTitleY = backgroundHeight - 94 // copied from MenuScreen.<init>
    }

    override fun drawBackground(context: DrawContext, delta: Float, mouseX: Int, mouseY: Int) {
        context.drawTexture(BACKGROUND_TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight)
    }

    override fun drawForeground(context: DrawContext, mouseX: Int, mouseY: Int) {
        context.drawText(textRenderer, title, titleX, titleY, Colors.WHITE, false)
        context.drawText(textRenderer, playerInventoryTitle, playerInventoryTitleX, playerInventoryTitleY, Colors.WHITE, false)
        context.drawText(textRenderer, SELLING_LABEL, 26 + 9 - textRenderer.getWidth(SELLING_LABEL) / 2, 25, Colors.WHITE, false)
        context.drawText(textRenderer, PRICE_LABEL, 26 + 9 - textRenderer.getWidth(PRICE_LABEL) / 2, 61, Colors.WHITE, false)
    }

    /**
     * Updates the trade selling/price [stack] in the specified [slot].
     * This function is mostly meant for item viewer drag-and-drop interactions.
     */
    fun updateTradeStack(slot: Slot, stack: ItemStack) {
        if (!TradingStationMenu.isValidItem(stack)) {
            LOGGER.error("Trying to set invalid item {} for slot {} in trading station", stack, slot)
            return
        }

        slot.stack = stack
        ClientNetworkBridge.get().sendSetTradeStack(menu.syncId, slot.id, stack)
    }

    companion object {
        private val LOGGER = logger()
        private val BACKGROUND_TEXTURE = AdornCommon.id("textures/gui/trading_station.png")
        private val SELLING_LABEL: Text = Text.translatable("block.adorn.trading_station.selling")
        private val PRICE_LABEL: Text = Text.translatable("block.adorn.trading_station.price")
    }
}
