package juuxel.adorn.client

import juuxel.adorn.util.InlineServices
import juuxel.adorn.util.loadService
import net.minecraft.item.ItemStack

interface ClientNetworkBridge {
    fun sendSetTradeStack(syncId: Int, slotId: Int, stack: ItemStack)

    @InlineServices
    companion object {
        private val instance: ClientNetworkBridge by lazy { loadService() }
        fun get(): ClientNetworkBridge = instance
    }
}
