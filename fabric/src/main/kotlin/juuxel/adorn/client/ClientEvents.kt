package juuxel.adorn.client

import juuxel.adorn.client.gui.TradeTooltipComponent
import juuxel.adorn.trading.Trade
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback

object ClientEvents {
    fun init() {
        TooltipComponentCallback.EVENT.register { data ->
            when (data) {
                is Trade -> TradeTooltipComponent(data)
                else -> null
            }
        }
    }
}
