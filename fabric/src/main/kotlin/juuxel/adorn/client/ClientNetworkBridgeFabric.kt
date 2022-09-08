package juuxel.adorn.client

import juuxel.adorn.lib.AdornNetworking
import net.minecraft.item.ItemStack

class ClientNetworkBridgeFabric : ClientNetworkBridge {
    override fun sendSetTradeStack(syncId: Int, slotId: Int, stack: ItemStack) {
        AdornNetworking.sendSetTradeStack(syncId, slotId, stack)
    }
}
