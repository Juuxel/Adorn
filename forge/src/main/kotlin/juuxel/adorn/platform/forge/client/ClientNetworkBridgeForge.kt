package juuxel.adorn.platform.forge.client

import juuxel.adorn.client.ClientNetworkBridge
import juuxel.adorn.platform.forge.networking.AdornNetworking
import juuxel.adorn.platform.forge.networking.SetTradeStackC2SMessage
import net.minecraft.item.ItemStack

class ClientNetworkBridgeForge : ClientNetworkBridge {
    override fun sendSetTradeStack(syncId: Int, slotId: Int, stack: ItemStack) {
        AdornNetworking.CHANNEL.sendToServer(SetTradeStackC2SMessage(syncId, slotId, stack))
    }
}
