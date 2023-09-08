package juuxel.adorn.compat.emi

import dev.emi.emi.api.EmiDragDropHandler
import dev.emi.emi.api.stack.EmiIngredient
import juuxel.adorn.client.gui.screen.TradingStationScreen
import juuxel.adorn.menu.TradingStationMenu

object TradingStationDragDropHandler : EmiDragDropHandler<TradingStationScreen> {
    override fun dropStack(screen: TradingStationScreen, stack: EmiIngredient, x: Int, y: Int): Boolean {
        val itemStack = stack.emiStacks.singleOrNull()?.itemStack ?: return false
        if (itemStack.isEmpty || !TradingStationMenu.isValidItem(itemStack)) return false

        for (slot in arrayOf(screen.menu.sellingSlot, screen.menu.priceSlot)) {
            val slotX = slot.x + screen.panelX
            val slotY = slot.y + screen.panelY

            if (x in slotX until (slotX + 16) && y in slotY until (slotY + 16)) {
                screen.updateTradeStack(slot, itemStack)
                return true
            }
        }

        return false
    }
}
