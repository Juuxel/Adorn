package juuxel.adorn.client

import juuxel.adorn.util.ServiceDelegate
import net.minecraft.item.ItemStack

interface ClientNetworkBridge {
    fun sendSetTradeStack(syncId: Int, slotId: Int, stack: ItemStack)

    companion object {
        private val instance: ClientNetworkBridge by ServiceDelegate()
        fun get(): ClientNetworkBridge = instance
    }
}
