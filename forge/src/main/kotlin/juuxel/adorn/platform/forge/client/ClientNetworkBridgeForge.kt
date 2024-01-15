package juuxel.adorn.platform.forge.client

import juuxel.adorn.client.ClientNetworkBridge
import juuxel.adorn.platform.forge.networking.SetTradeStackC2SMessage
import net.minecraft.item.ItemStack
import net.neoforged.neoforge.network.PacketDistributor

class ClientNetworkBridgeForge : ClientNetworkBridge {
    override fun sendSetTradeStack(syncId: Int, slotId: Int, stack: ItemStack) {
        PacketDistributor.SERVER.noArg().send(SetTradeStackC2SMessage(syncId, slotId, stack))
    }
}
